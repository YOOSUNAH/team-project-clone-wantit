package io.dcns.wantitauction.domain.user.service;


import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER;
import static io.dcns.wantitauction.domain.user.UserCommonTest.TEST_USER_SIGNUP_REQUEST_DTO;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.dcns.wantitauction.domain.user.UserTestUtils;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
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

    @DisplayName("회원 가입")
    @Test
    void signup() {

        // given
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(userRepository.existsByNickname(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("encodedPassword");

        var testUser = UserTestUtils.get(TEST_USER);

        // when, then
        assertDoesNotThrow(
            () -> userService.signup(TEST_USER_SIGNUP_REQUEST_DTO)
        );

        verify(userRepository, times(1)).save(any());

    }

    @Test
    void login() {
    }

    @Test
    void logout() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void updatePassword() {
    }

    @Test
    void deleteUser() {
    }
}
