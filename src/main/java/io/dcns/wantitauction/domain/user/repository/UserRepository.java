package io.dcns.wantitauction.domain.user.repository;

import io.dcns.wantitauction.domain.user.entity.User;
import java.util.Optional;

public interface UserRepository {

    boolean checkEmail(String email);

    void save(User user);

    Optional<User> findByEmail(String email);

    Optional<User> findByUserId(Long userId);

    void delete(User user);

    boolean existsByNickname(String nickname);
}
