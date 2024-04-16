package io.dcns.wantitauction.domain.user.dto;

import io.dcns.wantitauction.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
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
