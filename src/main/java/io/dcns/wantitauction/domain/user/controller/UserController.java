package io.dcns.wantitauction.domain.user.controller;

import io.dcns.wantitauction.domain.user.dto.LoginRequestDto;
import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.service.UserService;
import io.dcns.wantitauction.global.dto.ResponseDto;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseDto<Void>> signup(@Valid @RequestBody SignupRequestDto request) {
        userService.signup(request);
        return ResponseDto.of(HttpStatus.OK, null);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto<Void>> login(
        @Valid @RequestBody LoginRequestDto request, HttpServletResponse response
    ) {
        String token = userService.login(request);

        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        return ResponseDto.of(HttpStatus.OK, null);
    }
}
