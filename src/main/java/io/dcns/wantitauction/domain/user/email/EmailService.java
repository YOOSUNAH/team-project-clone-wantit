package io.dcns.wantitauction.domain.user.email;

import io.dcns.wantitauction.global.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
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

        return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)  // max size
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    private String setContext(String code) {
        // ClassLoaderTemplateResolver, TemplateEngine를 통해서 email.html을 spring과 연결해준다.
        Context context = new Context();  // thymeleaf Context를 통해 매개변수를 Thymeleaf안에 할당해준다.
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();

        context.setVariable("code", code);

        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCacheable(false);

        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process("mail", context);
    }

    // 메일 반환
    private MimeMessage createEmailForm(String email) throws MessagingException {
        // 난수 생성
        String authCode = createdCode();

        // MimeMessage 객체 안에, 코드, 송신 이메일, Context를 담아주고
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(RecipientType.TO, email);  // 이메일 수신자 설정
        message.setSubject("Want It 회원가입 인증 번호 입니다.");  // 이메일 제목
        message.setFrom(configEmail);  // 이메일 발신자 주소 설정
        message.setText(setContext(authCode), "utf-8",
            "html");  // 이메일 본문 설정 :  utf-8" 인코딩을 사용하며, 메시지 형식은 "html"

        // redis에 난수와 수신 이메일을 저장한다.
        redisUtil.setDataExpire(email, authCode, 60 * 30L);

        return message;
    }

    // 메일 전송 (JavaMailSender를 이용하여)
    public void sendEmail(String toEmail) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }
        MimeMessage emailForm = createEmailForm(toEmail);

        javaMailSender.send(emailForm);
    }

    // 코드 검증  (보낸 이메일과 코드가 일치하는지 검증)
    public Boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            return false;
        }
        return codeFoundByEmail.equals(code);
    }

    // 모든 인증 성공 시 memgerId를 생성해서 보내줌
    public String makeMemberId(String email) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(
            "SHA-256"); // 해시 암호화 를 사용하며 로직은 SHA-256을 사용한다.
        messageDigest.update(email.getBytes());
        messageDigest.update(LocalDateTime.now().toString().getBytes());

        StringBuilder builder = new StringBuilder();
        for (byte b : messageDigest.digest()) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}



