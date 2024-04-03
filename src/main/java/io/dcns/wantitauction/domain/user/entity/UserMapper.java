package io.dcns.wantitauction.domain.user.entity;

import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;


public class UserMapper {

    public static User SignupRequestDtoToUser(SignupRequestDto signupRequestDto,
        String encodedPassword) {
        return new User(
            signupRequestDto.getEmail(),
            encodedPassword,
            signupRequestDto.getUsername(),
            signupRequestDto.getNickname(),
            signupRequestDto.getPhoneNumber(),
            signupRequestDto.getAddress()
        );
    }
}
