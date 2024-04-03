package io.dcns.wantitauction.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotBlank
    @Email(message = "이메일 형식을 지켜주세요.")
    private String email;

    @NotBlank
    private String password;
}
