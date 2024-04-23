package io.dcns.wantitauction.domain.user.controller;

import io.dcns.wantitauction.domain.user.dto.LoginRequestDto;
import io.dcns.wantitauction.domain.user.dto.PasswordRequestDto;
import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserUpdateResponseDto;
import io.dcns.wantitauction.domain.user.service.UserService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ResponseDto<Void>> signup(
        @Valid @RequestBody SignupRequestDto signupRequestDto) {
        userService.signup(signupRequestDto);
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<String>> login(
        @Valid @RequestBody LoginRequestDto request, HttpServletResponse response
    ) {
        String accessToken = userService.login(request);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        log.info("로그인 API 성공 _  accessToken: {}", accessToken);
        return ResponseDto.of(HttpStatus.OK, accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDto<Void>> logout(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.logout(userDetails.getUser().getUserId());
        log.info("로그아웃 API 성공");
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @PutMapping
    public ResponseEntity<ResponseDto<UserUpdateResponseDto>> updateProfile(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody UserRequestDto userRequestDto) {
        UserUpdateResponseDto userUpdateResponseDto = userService.updateUser(userDetails, userRequestDto);
        log.info("유저 정보 변경 API 성공");
        return ResponseDto.of(HttpStatus.OK, userUpdateResponseDto);
    }

    @PatchMapping("/password")
    public ResponseEntity<ResponseDto<Void>> updatePassword(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody PasswordRequestDto passwordRequestDto) {
        userService.updatePassword(userDetails, passwordRequestDto);
        log.info("유저 비밀번호 변경 API 성공");
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @PatchMapping
    public ResponseEntity<ResponseDto<Void>> deleteUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getUser().getUserId());
        log.info("회원 탈퇴 API 성공");
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @GetMapping
    public ResponseEntity<ResponseDto<UserUpdateResponseDto>> getOneUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails){
        UserUpdateResponseDto user = userService.getUser(userDetails);
        log.info("유저 정보 조회 API 성공");
        return ResponseDto.of(HttpStatus.OK, user);
    }
}
