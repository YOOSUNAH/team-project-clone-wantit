package io.dcns.wantitauction.domain.user;

import io.dcns.wantitauction.domain.user.dto.LoginRequestDto;
import io.dcns.wantitauction.domain.user.dto.PasswordRequestDto;
import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserRequestDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;

public interface UserCommonTest {

    String ANOTHER_PREFIX = "another-";
    String DIFFERENT_PREFIX = "different-";
    Long TEST_USER_ID = 1L;
    UserRoleEnum USER_ROLE = UserRoleEnum.USER;
    String TEST_USER_NAME = "username12";
    String TEST_USER_PASSWORD = "password12^^";
    String TEST_USER_EMAIL = "sonny12@gmail.com";
    String TEST_USER_NICKNAME = "nickname";
    String TEST_USER_PHONENUMBER = "010-1234-5678";
    String TEST_USER_ADDRESS = "대한민구 서울 용산구";
    String TOKEN = "test-token";

    int PAGE = 0;
    int SIZE = 10;
    int EXPECTED_TOTAL_PAGE = 1;

    User TEST_USER = User.builder()
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_PASSWORD)
        .username(TEST_USER_NAME)
        .nickname(TEST_USER_NICKNAME)
        .phoneNumber(TEST_USER_PHONENUMBER)
        .address(TEST_USER_ADDRESS)
        .role(USER_ROLE)
        .build();

    UserDetailsImpl TEST_USER_DETAILS = new UserDetailsImpl(TEST_USER);

    SignupRequestDto TEST_USER_SIGNUP_REQUEST_DTO = SignupRequestDto.builder()
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_PASSWORD)
        .username(TEST_USER_NAME)
        .nickname(TEST_USER_NICKNAME)
        .phoneNumber(TEST_USER_PHONENUMBER)
        .address(TEST_USER_ADDRESS)
        .role(String.valueOf(USER_ROLE))
        .build();

    LoginRequestDto TEST_USER_LOGIN_REQUEST_DTO = LoginRequestDto.builder()
        .email(TEST_USER_EMAIL)
        .password(TEST_USER_PASSWORD)
        .build();

    UserRequestDto TEST_USER_REQUEST_DTO = UserRequestDto.builder()
        .nickname(TEST_USER_NICKNAME)
        .phoneNumber(TEST_USER_PHONENUMBER)
        .address(TEST_USER_ADDRESS)
        .build();

    PasswordRequestDto TEST_PASSWORD_REQUEST_DTO = PasswordRequestDto.builder()
        .password(TEST_USER_PASSWORD)
        .changePassword(ANOTHER_PREFIX + TEST_USER_PASSWORD)
        .rechangePassword(ANOTHER_PREFIX + TEST_USER_PASSWORD)
        .build();


    PasswordRequestDto TEST_WRONG_PASSWORD_REQUEST_DTO = PasswordRequestDto.builder()
        .password(TEST_USER_PASSWORD)
        .changePassword(ANOTHER_PREFIX + TEST_USER_PASSWORD)
        .rechangePassword(DIFFERENT_PREFIX + TEST_USER_PASSWORD)
        .build();
}
