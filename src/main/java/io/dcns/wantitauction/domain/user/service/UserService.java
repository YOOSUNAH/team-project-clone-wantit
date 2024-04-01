package io.dcns.wantitauction.domain.user.service;

import io.dcns.wantitauction.domain.user.dto.LoginRequestDto;
import io.dcns.wantitauction.domain.user.dto.LoginResponseDto;
import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import io.dcns.wantitauction.global.exception.NotMatchException;
import io.dcns.wantitauction.global.exception.UserNotFoundException;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import io.dcns.wantitauction.global.jwt.repository.TokenRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    public void signup(SignupRequestDto request) {
        if (userRepository.checkEmail(request.getEmail())) {
            throw new EntityExistsException("해당 이메일이 존재합니다.");
        }
        SignupRequestDto signupDto = new SignupRequestDto(request.getEmail(),
            request.getPassword());
        userRepository.signup(signupDto);
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail()).orElseThrow(
            () -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new NotMatchException("비밀번호가 일치하지 않습니다.");
        }

        String token = generateToken(user.getUserId());
        return new LoginResponseDto(user.getEmail(), token);
    }

    private String generateToken(Long userId) {
        String refreshToken = jwtUtil.generateRefreshToken(userId, "User");
        refreshToken = jwtUtil.substringToken(refreshToken);
        tokenRepository.register(userId, refreshToken);

        return jwtUtil.generateAccessToken(userId, "User");
    }

}
