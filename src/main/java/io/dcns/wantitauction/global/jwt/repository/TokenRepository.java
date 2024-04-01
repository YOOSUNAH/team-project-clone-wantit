package io.dcns.wantitauction.global.jwt.repository;

import io.dcns.wantitauction.global.jwt.entity.RefreshTokenEntity;
import java.util.List;

public interface TokenRepository {

    void register(Long userId, String token);

    RefreshTokenEntity findByUserId(Long userId);

    List<RefreshTokenEntity> findAllByUserId(Long userId);

    void deleteToken(RefreshTokenEntity token);
}
