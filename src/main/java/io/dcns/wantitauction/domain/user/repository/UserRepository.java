package io.dcns.wantitauction.domain.user.repository;

import io.dcns.wantitauction.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
