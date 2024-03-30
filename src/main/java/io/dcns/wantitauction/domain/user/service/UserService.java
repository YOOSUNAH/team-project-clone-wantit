package io.dcns.wantitauction.domain.user.service;

import io.dcns.wantitauction.domain.user.dto.NicknameUpdateRequest;
import io.dcns.wantitauction.domain.user.dto.PasswordUpdateRequest;
import io.dcns.wantitauction.domain.user.dto.UserSignupRequestDto;
import io.dcns.wantitauction.domain.user.entity.User;
import io.dcns.wantitauction.domain.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
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

    public void signup(UserSignupRequestDto request) {
        String email = request.getEmail();
        String password = passwordEncoder.encode(request.getPassword());
        String nickname = request.getNickname();

        validateEmailDuplicate(email);
        validateNicknameDuplicate(nickname);

        User user = new User(email, password, nickname);
        userRepository.save(user);
    }

    public void updatePassword(PasswordUpdateRequest request, User user) {
        User target = validateUser(user);
        String oldPassword = target.getPassword();
        String newPassword = request.getNewPassword();

        validateNewPassword(newPassword, oldPassword);

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        target.setPassword(encodedNewPassword);
    }

    public void updateNickname(NicknameUpdateRequest request, User user) {
        User target = validateUser(user);
        String newNickname = request.getNewNickname();

        validateNicknameDuplicate(newNickname);

        target.setNickname(newNickname);
    }

    public void delete(User user) {
        User target = validateUser(user);

        validateDeleted(target);

        userRepository.deleteById(target.getUserId());
    }

    private void validateEmailDuplicate(String request) {
        if (userRepository.existsByEmail(request)) {
            throw new EntityExistsException("중복된 사용자가 존재합니다.");
        }
    }

    private void validateNicknameDuplicate(String request) {
        if (userRepository.existsByNickname(request)) {
            throw new EntityExistsException("중복된 닉네임이 존재합니다.");
        }
    }

    private User validateUser(User user) {
        return userRepository.findById(user.getUserId())
            .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));
    }

    private void validateNewPassword(String newPassword, String oldPassword) {
        if (passwordEncoder.matches(newPassword, oldPassword)) {
            throw new IllegalArgumentException("현재 비밀번호와 동일합니다.");
        }
    }

    private static void validateDeleted(User target) {
        if (target.getDeletedAt() != null) {
            throw new EntityNotFoundException("이미 삭제된 사용자입니다.");
        }
    }
}
