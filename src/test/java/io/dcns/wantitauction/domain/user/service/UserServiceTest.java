package io.dcns.wantitauction.domain.user.service;


import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_PASSWORD_REQUEST_DTO;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_DETAILS;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_ID;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_LOGIN_REQUEST_DTO;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_REQUEST_DTO;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_SIGNUP_REQUEST_DTO;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_WRONG_PASSWORD_REQUEST_DTO;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TOKEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.dcns.wantitauction.domain.user.UserTestUtils;
import io.dcns.wantitauction.domain.user.dto.UserResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import io.dcns.wantitauction.global.exception.NotMatchException;
import io.dcns.wantitauction.global.exception.UserNotFoundException;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import io.dcns.wantitauction.global.jwt.RefreshTokenRepository;
import jakarta.persistence.EntityExistsException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    UserService userService;

    @Mock
    UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RefreshTokenRepository refreshTokenRepository;


    @Mock
    JwtUtil jwtUtil;

    @Nested
    @DisplayName("회원 가입 관련 테스트")
    class SignUpTest {

        @DisplayName("회원 가입")
        @Test
        void signup() {
            // given
            given(userRepository.existsByEmail(anyString())).willReturn(false);
            given(userRepository.existsByNickname(anyString())).willReturn(false);
            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

            // when, then
            assertDoesNotThrow(
                () -> userService.signup(TEST_USER_SIGNUP_REQUEST_DTO)
            );
            verify(userRepository, times(1)).save(any());
        }

        @DisplayName("회원 가입 실패 - 중복된 사용자")
        @Test
        void signup_fail_duplicateUser() {
            // given
            given(userRepository.existsByEmail(anyString())).willReturn(true);

            // when, then
            assertThrows(EntityExistsException.class,
                () -> userService.signup(TEST_USER_SIGNUP_REQUEST_DTO)
            );
        }

        @DisplayName("회원 가입 실패 - 중복된 Nickname")
        @Test
        void signup_fail_duplicateNickname() {
            // given
            given(userRepository.existsByNickname(anyString())).willReturn(true);

            // when, then
            assertThrows(EntityExistsException.class,
                () -> userService.signup(TEST_USER_SIGNUP_REQUEST_DTO)
            );
        }
    }

    @Nested
    @DisplayName("로그인/로그아웃 관련 테스트")
    class LoginTest {

        @DisplayName("로그인")
        @Test
        void login() {
            // given
            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(TEST_USER));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            given(
                jwtUtil.generateAccessAndRefreshToken(nullable(Long.class), eq(UserRoleEnum.USER)))
                .willReturn(TOKEN);

            // when
            String token = userService.login(TEST_USER_LOGIN_REQUEST_DTO);

            // then
            assertNotNull(token);
        }

        @DisplayName("로그인 실패 - user가 존재하지 않을때")
        @Test
        void login_fail() {
            // given
            given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

            // when, then
            assertThrows(UserNotFoundException.class,
                () -> userService.login(TEST_USER_LOGIN_REQUEST_DTO)
            );
        }

        @DisplayName("로그인 실패 - password가 일치하지 않을때")
        @Test
        void login_fail_by_password() {
            // given
            given(userRepository.findByEmail(anyString())).willReturn(Optional.of(TEST_USER));
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            // when, then
            assertThrows(NotMatchException.class,
                () -> userService.login(TEST_USER_LOGIN_REQUEST_DTO)
            );
        }

        @DisplayName("로그아웃")
        @Test
        void logout() {
            // given
            given(refreshTokenRepository.findByUserId(anyLong())).willReturn(TOKEN);

            // when, then
            assertDoesNotThrow(
                () -> userService.logout(TEST_USER_ID)
            );
            verify(refreshTokenRepository, times(1)).delete(TOKEN);
        }
    }

    @Nested
    @DisplayName("정보 변경 관련 테스트")
    class UpdateUserInfoTest {

        @DisplayName("프로필 수정")
        @Test
        void updateUser() {
            //given
            User testUser = UserTestUtils.get(TEST_USER);
            given(userRepository.findByUserId(testUser.getUserId())).willReturn(
                Optional.of(testUser));

            UserDetailsImpl testUserDetails = TEST_USER_DETAILS;
            testUserDetails.getUser().setUserId(1L);

            given(userRepository.existsByNickname(anyString())).willReturn(false);

            // when
            var result = userService.updateUser(testUserDetails, TEST_USER_REQUEST_DTO);

            // then
            UserResponseDto userResponseDto = new UserResponseDto(
                testUser.getNickname(),
                testUser.getPhoneNumber(),
                testUser.getAddress()
            );
            assertThat(result.nickname()).isEqualTo(userResponseDto.nickname());
            assertThat(result.phoneNumber()).isEqualTo(userResponseDto.phoneNumber());
            assertThat(result.address()).isEqualTo(userResponseDto.address());
        }

        @DisplayName("프로필 수정-실패 - 중복된 nickName")
        @Test
        void updateUserFail() {
            //given
            User testUser = UserTestUtils.get(TEST_USER);
            given(userRepository.findByUserId(testUser.getUserId())).willReturn(
                Optional.of(testUser));

            UserDetailsImpl testUserDetails = TEST_USER_DETAILS;
            testUserDetails.getUser().setUserId(1L);

            given(userRepository.existsByNickname(anyString())).willReturn(true);

            // when, then
            assertThrows(EntityExistsException.class,
                () -> userService.updateUser(testUserDetails, TEST_USER_REQUEST_DTO)
            );
        }

        @DisplayName("프로필 수정 - 비밀번호")
        @Test
        void updatePassword() {
            // given
            User testUser = UserTestUtils.get(TEST_USER);
            given(userRepository.findByUserId(testUser.getUserId())).willReturn(
                Optional.of(testUser));

            UserDetailsImpl testUserDetails = TEST_USER_DETAILS;
            testUserDetails.getUser().setUserId(1L);

            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
            given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

            // when
            assertDoesNotThrow(
                () -> userService.updatePassword(testUserDetails, TEST_PASSWORD_REQUEST_DTO)
            );

            // then
            verify(passwordEncoder, times(1)).encode(TEST_PASSWORD_REQUEST_DTO.getChangePassword());
        }

        @DisplayName("프로필 수정 - 이전 비밀번호 확인 실패")
        @Test
        void updatePasswordFail() {
            // given
            User testUser = UserTestUtils.get(TEST_USER);
            given(userRepository.findByUserId(testUser.getUserId())).willReturn(
                Optional.of(testUser));

            UserDetailsImpl testUserDetails = TEST_USER_DETAILS;
            testUserDetails.getUser().setUserId(1L);

            // when
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

            // then
            assertThrows(NotMatchException.class,
                () -> userService.updatePassword(testUserDetails, TEST_PASSWORD_REQUEST_DTO)
            );
        }

        @DisplayName("프로필 수정 - 바꾸려는 비밀번호 확인 실패")
        @Test
        void updatePasswordFailDuplilcate() {
            // given
            User testUser = UserTestUtils.get(TEST_USER);
            given(userRepository.findByUserId(testUser.getUserId())).willReturn(
                Optional.of(testUser));

            UserDetailsImpl testUserDetails = TEST_USER_DETAILS;
            testUserDetails.getUser().setUserId(1L);

            // when
            given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

            // then
            assertThrows(NotMatchException.class,
                () -> userService.updatePassword(testUserDetails, TEST_WRONG_PASSWORD_REQUEST_DTO)
            );
        }
    }

    @DisplayName("회원 탈퇴")
    @Test
    void deleteUser() {
        // given
        given(userRepository.findByUserId(anyLong())).willReturn(Optional.of(TEST_USER));

        // when, then
        assertDoesNotThrow(
            () -> userService.deleteUser(TEST_USER_ID)
        );

        verify(userRepository, times(1)).delete(TEST_USER);
    }
}

