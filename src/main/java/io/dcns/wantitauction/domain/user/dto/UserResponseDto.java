package io.dcns.wantitauction.domain.user.dto;

import io.dcns.wantitauction.domain.admin.dto.UsersResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import lombok.AllArgsConstructor;
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
}


