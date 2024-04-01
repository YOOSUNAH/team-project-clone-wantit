package io.dcns.wantitauction.domain.user.repository;

import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean checkEmail(String email) {
        return userJpaRepository.findByEmail(email).isPresent();
    }

    @Override
    public void signup(SignupRequestDto signupRequestDto) {
        User user = new User(signupRequestDto);
        userJpaRepository.save(user);
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

    @Override
    public void delete(User user) {
        userJpaRepository.delete(user);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }

}

