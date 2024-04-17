package io.dcns.wantitauction.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    private final String nickname;
    private final String phoneNumber;
    private final String address;

    public UserResponseDto(String nickname, String phoneNumber, String address ) {
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }
}


