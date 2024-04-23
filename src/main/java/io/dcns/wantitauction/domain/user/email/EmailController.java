package io.dcns.wantitauction.domain.user.email;

import io.dcns.wantitauction.global.dto.ResponseDto;
import jakarta.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/emails")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/authcode")
    public ResponseEntity<ResponseDto<String>> sendEmailPath(
        @RequestBody EmailSignupRequestDto requestDto) throws MessagingException {

        emailService.sendEmail(requestDto.getEmail());
        log.info("이메일 인증하기 API 성공");
        return ResponseDto.of(HttpStatus.OK, "이메일을 확인하세요");
    }

    @PostMapping("/authcode/verify")
    public ResponseEntity<ResponseDto<Boolean>> sendEmailAndCode(
        @RequestBody EmailRequestDto requestDto) throws NoSuchAlgorithmException {

        if (emailService.verifyEmailCode(requestDto.getEmail(), requestDto.getCode())) {
            log.info("인증번호가 일치합니다.");
            return ResponseDto.of(HttpStatus.OK, true);
        }
        log.info("인증번호가 일치하지 않습니다.");
        return ResponseDto.of(HttpStatus.NOT_FOUND, false);
    }
}
