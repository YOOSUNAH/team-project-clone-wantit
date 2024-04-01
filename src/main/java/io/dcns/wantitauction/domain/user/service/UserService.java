package io.dcns.wantitauction.domain.user.service;

import io.dcns.wantitauction.domain.user.dto.LoginRequestDto;
import io.dcns.wantitauction.domain.user.dto.PasswordRequestDto;
import io.dcns.wantitauction.domain.user.dto.PasswordResponseDto;
import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import io.dcns.wantitauction.global.exception.NotMatchException;
import io.dcns.wantitauction.global.exception.UserNotFoundException;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import io.dcns.wantitauction.global.jwt.entity.RefreshTokenEntity;
import io.dcns.wantitauction.global.jwt.repository.TokenRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final TokenRepository tokenRepository;

    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {
        if (userRepository.checkEmail(signupRequestDto.getEmail())) {
            throw new EntityExistsException("해당 이메일이 존재합니다.");
        }

        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(signupRequestDto);
        user.setPassword(password);
        userRepository.save(user);
    }

    @Transactional
    public String login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(
            () -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new NotMatchException("비밀번호가 일치하지 않습니다.");
        }

        String token = generateToken(user.getUserId());
        return token;
    }

    @Transactional
    public void logout(Long userId) {
        List<RefreshTokenEntity> refreshTokenEntityList = tokenRepository.findAllByUserId(userId);
        refreshTokenEntityList.forEach(tokenRepository::deleteToken);
    }

    @Transactional
    public UserResponseDto updateUser(UserDetailsImpl userDetails, UserRequestDto userRequestDto) {
        // 토큰으로 id 가져오기
        Long userId = userDetails.getUser().getUserId();
        // DB에 접근
        User user = getUser(userId);
        //nickname 확인
        if (!user.getNickname().equals(userRequestDto.getNickname())) {
            validateNicknameDuplicate(userRequestDto.getNickname());
        }
        // 변경
        user.update(userRequestDto);
        return new UserResponseDto(user);
    }

    @Transactional
    public void updatePassword(Long userId, PasswordRequestDto passwordRequestDto) {
        User user = getUser(userId);

        if (!passwordEncoder.matches(passwordRequestDto.getPassword(), user.getPassword())) {
            throw new NotMatchException("비밀번호가 일치하지 않습니다.");
        }
        PasswordResponseDto passwordResponseDto = new PasswordResponseDto(
            passwordRequestDto);
        passwordResponseDto.checkChangePasswordEquals();
        user.updatePassword(passwordEncoder.encode(passwordResponseDto.getChangePassword()));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = getUser(userId);
        userRepository.delete(user);
    }

    private String generateToken(Long userId) {
        String refreshToken = jwtUtil.generateRefreshToken(userId, "User");
        refreshToken = jwtUtil.substringToken(refreshToken);
        tokenRepository.register(userId, refreshToken);

        return jwtUtil.generateAccessToken(userId, "User");
    }

    private User getUser(Long userId) {
        return userRepository.findByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
    }

    private void validateNicknameDuplicate(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new EntityExistsException("중복된 닉네임입니다.");
        }
    }
}
