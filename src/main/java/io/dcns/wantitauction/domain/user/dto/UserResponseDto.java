package io.dcns.wantitauction.domain.user.dto;

import io.dcns.wantitauction.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String nickname;
    private final String phoneNumber;
    private final String address;

    public UserResponseDto(User user) {
        nickname = user.getNickname();
        phoneNumber = user.getPhoneNumber();
        address = user.getAddress();
    }
}
