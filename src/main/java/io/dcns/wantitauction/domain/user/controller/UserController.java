package io.dcns.wantitauction.domain.user.controller;

import io.dcns.wantitauction.domain.user.dto.LoginRequestDto;
import io.dcns.wantitauction.domain.user.dto.LoginResponseDto;
import io.dcns.wantitauction.domain.user.dto.UserSignupRequestDto;
import io.dcns.wantitauction.domain.user.service.UserService;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public void signup(@Valid @RequestBody UserSignupRequestDto request) {
        userService.signup(request);
    }

    @PostMapping("/login")
    public void login(
        @Validated @RequestBody LoginRequestDto request,
        HttpServletResponse response
    ) {
        LoginResponseDto responseDto = userService.login(request);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, responseDto.getToken());
    }


}