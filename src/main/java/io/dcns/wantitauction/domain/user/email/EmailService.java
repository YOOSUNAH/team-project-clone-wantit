package io.dcns.wantitauction.domain.user.email;

import io.dcns.wantitauction.global.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    @Value("${spring.mail.username}")  //yml에서 추출
    private String configEmail;

    // 난수 생성 : 0~9와 a~z까지의 숫자와 문자를 섞어서 6자리 난수 생성
    private String createdCode() {
        int leftLimit = 48;  // 숫자 0
        int rightLimit = 122; // 알파벳 Z
        int targetStringLength = 6; // 6자리
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65) && (i <= 90
                || i >= 97))  // 정수 중 숫자(48-57)와 대문자 알파벳(65-90), 소문자 알파벳(97-122)만을 선택하기 위한 필터
            .limit(targetStringLength)  // max size
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    // 메일 전송 (JavaMailSender를 이용하여)
    public void sendEmail(String toEmail) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }
        MimeMessage emailForm = createEmailForm(toEmail);

        javaMailSender.send(emailForm);
    }

    // 메일 반환
    private MimeMessage createEmailForm(String email) throws MessagingException {
        // 난수 생성
        String authCode = createdCode();

        // MimeMessage 객체 안에, 코드, 송신 이메일, Context를 담아주고
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(RecipientType.TO, email);  // 이메일 수신자 설정  (수신자, 수신자 email주소)
        message.setSubject("Want It 회원가입 인증 번호 입니다.");  // 이메일 제목
        message.setFrom(configEmail);  // 이메일 발신자 주소 (yml에서 추출한 configEmail)
        message.setText(setContext(authCode), "utf-8",
            "html");  // 이메일 본문 설정 :  utf-8" 인코딩을 사용하며, 메시지 형식은 "html"

        // redis에 난수와 수신 이메일을 저장한다.
        redisUtil.setDataExpire(email, authCode,
            60 * 30L); // key로 사용될 받는 이의 email주소, 난수, 만료기간(30분) redis에 저장

        return message;
    }

    // Thymeleaf 템플릿 엔진을 사용하여 HTML 이메일 본문을 생성
    private String setContext(String code) {
        // ClassLoaderTemplateResolver, TemplateEngine를 통해서 email.html을 spring과 연결해준다.
        Context context = new Context();  // thymeleaf Context 객체를 생성 (템플릿 내에 데이터를 전달하는데 사용)
        TemplateEngine templateEngine = new TemplateEngine(); // Thymeleaf의 TemplateEngine 객체를 생성 (템플릿을 처리하고 최종 결과물인 문자열을 생성하는 데 사용)
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver(); // ClassLoaderTemplateResolver 객체를 생성(템플릿 파일을 로드하는 데 사용)

        context.setVariable("code", code);  // createEmailForm에서 난수를 전달 받음.

        templateResolver.setPrefix(
            "templates/");  // 템플릿 파일을 찾을 때 사용할 경로의 접두사(prefix)로 "templates/"를 설정 (resources/templates/ 경로에 있는 파일을 찾음)
        templateResolver.setSuffix(".html");       // 템플릿 파일의 확장자로 .html을 설정
        templateResolver.setTemplateMode("HTML"); // 템플릿 파일의 형식으로 HTML으로 설정해서, HTML 문서로 해석되어야 한다고 설정
        templateResolver.setCacheable(false);     // 캐싱을 (비활성화)사용하지 않도록 설정

        templateEngine.setTemplateResolver(
            templateResolver);  // 위에서 설정한 파일 찾는 방법으로 templateEngine이 템플릿이 찾고 로드한다.

        return templateEngine.process("mail",
            context);  // process메서드를 통해서, mail이라는 이름의 템플릿을, context객체를 전달한다.
        // 처리된 템플릿은 문자열로 반환되고, 이 문자열은 이메일 본문으로 사용된다.
    }

    // 코드 검증 (보낸 이메일과 코드가 일치하는지 검증)
    public Boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            return false;
        }
        return codeFoundByEmail.equals(code);
    }
}



