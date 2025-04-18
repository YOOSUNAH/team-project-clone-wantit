package io.dcns.wantitauction.domain.user.service;


import io.dcns.wantitauction.domain.user.dto.LoginRequestDto;
import io.dcns.wantitauction.domain.user.dto.PasswordRequestDto;
import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserRequestDto;
import io.dcns.wantitauction.domain.user.dto.UserUpdateResponseDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.entity.UserMapper;
import io.dcns.wantitauction.domain.user.entity.UserRoleEnum;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import io.dcns.wantitauction.global.exception.NotMatchException;
import io.dcns.wantitauction.global.exception.UserNotFoundException;
import io.dcns.wantitauction.global.impl.UserDetailsImpl;
import io.dcns.wantitauction.global.jwt.JwtUtil;
import io.dcns.wantitauction.global.jwt.RefreshTokenRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(SignupRequestDto requestDto) {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new EntityExistsException("해당 이메일이 존재합니다.");
        }
        if (requestDto.getNickname() != null && userRepository.existsByNickname(
                requestDto.getNickname())) {
            throw new EntityExistsException("해당 Nickname이 존재합니다.");
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = UserMapper.SignupRequestDtoToUser(requestDto, encodedPassword);

        UserRoleEnum role = UserRoleEnum.valueOf(requestDto.getRole());
        user.setRole(role);
        userRepository.save(user);
    }

    @Transactional
    public String login(LoginRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new UserNotFoundException("해당 유저가 존재하지 않습니다."));
        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new NotMatchException("비밀번호가 일치하지 않습니다.");
        }

        Long userId = user.getUserId();
        UserRoleEnum role = user.getRole();
        return jwtUtil.generateAccessAndRefreshToken(userId, role);
    }

    @Transactional
    public void logout(Long userId) {
        String refreshToken = refreshTokenRepository.findByUserId(userId);
        refreshTokenRepository.delete(refreshToken);
    }

    @Transactional
    public UserUpdateResponseDto updateUser(
            UserDetailsImpl userDetails,
            UserRequestDto requestDto) {
        User user = validateUser(userDetails);
        validateNicknameDuplicate(requestDto.getNickname());

        user.update(requestDto.getNickname(), requestDto.getPhoneNumber(), requestDto.getAddress());
        return new UserUpdateResponseDto(requestDto.getNickname(), requestDto.getPhoneNumber(),
                requestDto.getAddress());
    }

    @Transactional
    public void updatePassword(
            UserDetailsImpl userDetails,
            PasswordRequestDto requestDto) {
        User user = validateUser(userDetails);

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new NotMatchException("비밀번호가 일치하지 않습니다.");
        }
        checkChangePasswordEquals(
                requestDto.getChangePassword(),
                requestDto.getCheckPassword());

        user.updatePassword(passwordEncoder.encode(requestDto.getChangePassword()));
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
        userRepository.delete(user);
    }

    private User validateUser(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getUserId();
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
    }

    private void validateNicknameDuplicate(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new EntityExistsException("중복된 닉네임입니다.");
        }
    }

    private void checkChangePasswordEquals(
            String changePassword,
            String rechangePassword) {
        if (!changePassword.equals(rechangePassword)) {
            throw new NotMatchException("바꿀 비밀번호가 일치하지 않습니다.");
        }
    }

    public UserUpdateResponseDto getUser(UserDetailsImpl userDetails) {
        Long userId = userDetails.getUser().getUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("선택한 유저가 존재하지 않습니다."));
        return new UserUpdateResponseDto(user.getNickname(), user.getPhoneNumber(), user.getAddress());
    }

    public User findByUserId(Long userId) {
        return userRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("존재하지 않는 사용자입니다."));
    }
}
