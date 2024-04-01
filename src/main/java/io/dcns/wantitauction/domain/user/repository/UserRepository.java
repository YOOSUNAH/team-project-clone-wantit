package io.dcns.wantitauction.domain.user.repository;

import io.dcns.wantitauction.domain.user.dto.SignupRequestDto;
import io.dcns.wantitauction.domain.user.entity.User;
import java.util.Optional;

public interface UserRepository {

    boolean checkEmail(String email);

    void signup(SignupRequestDto dto);
    Optional<User> findByEmail(String email);
    Optional<User> findByUserId(Long userId);
    Optional<User> findById(Long userId);

}
