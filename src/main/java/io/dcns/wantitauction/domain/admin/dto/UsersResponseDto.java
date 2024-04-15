package io.dcns.wantitauction.domain.admin.dto;

import io.dcns.wantitauction.domain.user.entity.User;
import lombok.Getter;

@Getter
public class UsersResponseDto {

    private Long userId;
    private String email;
    private String password;
    private String username;
    private String nickname;
    private String phoneNumber;
    private String address;
    private Long kakaoId;


    public UsersResponseDto(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.address = user.getAddress();
        this.kakaoId = user.getKakaoId();
    }
}


