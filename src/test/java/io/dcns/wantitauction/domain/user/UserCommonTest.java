package io.dcns.wantitauction.domain.user;

import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;

public interface UserCommonTest {

    UserRoleEnum USER_ROLE = UserRoleEnum.USER;
    String TEST_USER_NAME = "username12";
    String TEST_USER_PASSWORD = "password12^^";
    String TEST_USER_EMAIL = "sonny12@gmail.com";
    String TEST_USER_NICKNAME = "nickname";
    String TEST_USER_PHONENUMBER = "010-1234-5678";
    String TEST_USER_ADDRESS = "대한민구 서울 용산구";

    User TEST_USER = User.builder()
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_PASSWORD)
        .username(TEST_USER_NAME)
        .nickname(TEST_USER_NICKNAME)
        .phoneNumber(TEST_USER_PHONENUMBER)
        .address(TEST_USER_ADDRESS)
        .role(USER_ROLE)
        .build();

    SignupRequestDto TEST_USER_SIGNUP_REQUEST_DTO = SignupRequestDto.builder()
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_PASSWORD)
        .username(TEST_USER_NAME)
        .nickname(TEST_USER_NICKNAME)
        .phoneNumber(TEST_USER_PHONENUMBER)
        .address(TEST_USER_ADDRESS)
        .role(String.valueOf(USER_ROLE))
        .build();

}
