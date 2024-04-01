package io.dcns.wantitauction.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordRequestDto {

    @NotBlank
    private String password;

    @NotBlank
    private String changePassword;

    @NotBlank
    private String rechangePassword;


}
