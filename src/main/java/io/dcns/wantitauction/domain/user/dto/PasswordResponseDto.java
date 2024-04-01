package io.dcns.wantitauction.domain.user.dto;

import io.dcns.wantitauction.global.exception.NotMatchException;
import lombok.Getter;

@Getter
public class PasswordResponseDto {

    private final String password;
    private final String changePassword;
    private final String rechangePassword;

    public PasswordResponseDto(PasswordRequestDto passwordRequestDto) {
        password = passwordRequestDto.getPassword();
        changePassword = passwordRequestDto.getChangePassword();
        rechangePassword = passwordRequestDto.getRechangePassword();
    }

    public void checkChangePasswordEquals() {
        if (!changePassword.equals(rechangePassword)) {
            throw new NotMatchException("바꿀 비밀번호가 일치하지 않습니다.");
        }
    }
}
