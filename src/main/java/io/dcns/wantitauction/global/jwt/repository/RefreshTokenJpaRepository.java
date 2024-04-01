package io.dcns.wantitauction.global.jwt.repository;

import io.dcns.wantitauction.global.jwt.entity.RefreshTokenEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {

    Optional<RefreshTokenEntity> findByUserId(Long userId);

    List<RefreshTokenEntity> findAllByUserId(Long userId);
}
