package io.dcns.wantitauction.domain.user.dto;

import io.dcns.wantitauction.global.timestamp.Timestamped;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserSignupRequestDto extends Timestamped {

    @NotBlank
    @Email(message = "이메일 형식을 지켜주세요.")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;


}
