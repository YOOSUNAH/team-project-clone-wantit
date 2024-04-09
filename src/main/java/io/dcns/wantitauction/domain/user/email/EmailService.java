package io.dcns.wantitauction.domain.user.email;

import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import io.dcns.wantitauction.global.event.WinningBidEvent;
import io.dcns.wantitauction.global.exception.UserNotFoundException;
import io.dcns.wantitauction.global.util.RedisUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender emailSender;
    private final RedisUtil redisUtil;
    private final TemplateEngine templateEngine;
    private final UserRepository userRepository;

    @Value("${spring.mail.username}")  //yml에서 추출
    private String configEmail;

    public String createdCode() {   // 난수 생성 : 0~9와 a~z까지의 숫자와 문자를 섞어서 6자리 난수 생성
        int leftLimit = 48;  // 숫자 0
        int rightLimit = 122; // 알파벳 Z
        int targetStringLength = 6; // 6자리
        Random random = new Random();

        return random
            .ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90
                || i >= 97))  // 정수 중 숫자(48-57)와 대문자 알파벳(65-90), 소문자 알파벳(97-122)만을 선택하기 위한 필터
            .limit(targetStringLength)  // max size
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }

    // 메일 전송
    public void sendEmail(String email) throws MessagingException {
        if (redisUtil.existData(email)) {
            redisUtil.deleteData(email);
        }
        // 메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(email);
        emailSender.send(emailForm);
    }

    @EventListener(classes = {WinningBidEvent.class})
    private void sendEmailToWinner(WinningBidEvent winningBidEvent) throws MessagingException {
        User user = userRepository.findByUserId(winningBidEvent.getWinnerId()).orElseThrow(
            () -> new UserNotFoundException("존재하지 않는 유저입니다.")
        );
        StringBuffer buffer = new StringBuffer();
        buffer.append("경매 상품 : ").append(winningBidEvent.getItemName()).append("\n");
        buffer.append("낙찰 가격 : ").append(winningBidEvent.getWinPrice()).append("\n");

        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(RecipientType.TO, user.getEmail());
        message.setSubject("축하합니다! 참여하신 경매에 낙찰되셨습니다.");
        message.setFrom(configEmail);
        message.setText(buffer.toString(), "utf-8");
        emailSender.send(message);
    }

    // 메일 양식 작성
    public MimeMessage createEmailForm(String email) throws MessagingException {
        String authCode = createdCode();
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email);    // 받는 사람 설정
        message.setSubject("Want It 회원가입 인증 번호 입니다.");  // 이메일 제목
        message.setFrom(configEmail);        // 보내는 사람 설정
        message.setText(setContext(authCode), "utf-8",
            "html");  // 이메일 본문 설정 :  utf-8" 인코딩을 사용하며, 메시지 형식은 "html"

        redisUtil.setDataExpire(email, authCode, 60 * 30L);
        return message;
    }

    // Thymeleaf 템플릿 엔진을 사용하여 HTML 이메일 본문을 생성
    private String setContext(String code) {
        Context context = new Context();  // thymeleaf Context 객체를 생성 (템플릿 내에 데이터를 전달하는데 사용)
        context.setVariable("code", code);  // createEmailForm에서 난수를 전달 받음.
        return templateEngine.process("mail",
            context);  // process메서드를 통해서, mail이라는 이름의 템플릿을, context객체를 전달한다.
    }

    public Boolean verifyEmailCode(String email, String code) {
        String codeFoundByEmail = redisUtil.getData(email);
        if (codeFoundByEmail == null) {
            return false;
        }
        return codeFoundByEmail.equals(code);
    }
}



