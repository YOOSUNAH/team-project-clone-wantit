package io.dcns.wantitauction.domain.user.email;

import io.dcns.wantitauction.global.dto.ResponseDto;
import jakarta.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/emails")
public class EmailController {

    private final EmailService emailService;

    @GetMapping("/authcode")
    public ResponseEntity<ResponseDto<String>> sendEmailPath(
        @RequestBody EmailSignupRequestDto requestDto) throws MessagingException {

        emailService.sendEmail(requestDto.getEmail());
        return ResponseDto.of(HttpStatus.OK, "이메일을 확인하세요");
    }

    @PostMapping("/authcode")
    public ResponseEntity<ResponseDto<Boolean>> sendEmailAndCode(
        @RequestBody EmailRequestDto requestDto) throws NoSuchAlgorithmException {

        if (emailService.verifyEmailCode(requestDto.getEmail(), requestDto.getCode())) {
            return ResponseDto.of(HttpStatus.OK, true);
        }
        return ResponseDto.of(HttpStatus.NOT_FOUND, false);
    }
}
