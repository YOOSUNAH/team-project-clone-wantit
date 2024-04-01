package io.dcns.wantitauction.domain.user.dto;

import io.dcns.wantitauction.global.timestamp.Timestamped;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SignupRequestDto extends Timestamped {

    @NotBlank
    @Email(message = "이메일 형식을 지켜주세요.")
    private String email;

    @NotBlank
    private String password;

    public SignupRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
