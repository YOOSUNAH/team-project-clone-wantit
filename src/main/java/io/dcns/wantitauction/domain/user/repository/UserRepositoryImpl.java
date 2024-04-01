package io.dcns.wantitauction.domain.user.repository;

import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean checkEmail(String email) {
        return userJpaRepository.findByEmail(email).isPresent();
    }

    @Override
    public void signup(SignupRequestDto dto) {
        userJpaRepository.save(
        User.of(dto.getEmail(), passwordEncoder.encode(dto.getPassword()))
        );
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userJpaRepository.findById(userId);
    }

//    @Override
//    public void deleteById(Long userId) {
//
//    }
//
//    @Override
//    public boolean existsByEmail(String request) {
//        return false;
//    }
//
//    @Override
//    public boolean existsByNickname(String request) {
//        return false;
//    }
}

