package io.dcns.wantitauction.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Email(message = "이메일 형식을 지켜주세요.")
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String address;

    private String role;

    public void setRole(String role) {
        this.role = role != null ? role.toUpperCase() : "USER";
    }
}
