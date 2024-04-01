package io.dcns.wantitauction.global.jwt.repository;

import io.dcns.wantitauction.global.jwt.entity.RefreshTokenEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryImpl implements TokenRepository {

    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public void register(Long userId, String token) {
        RefreshTokenEntity entity = RefreshTokenEntity.of(userId, token);
        refreshTokenJpaRepository.save(entity);
    }

    @Override
    public RefreshTokenEntity findByUserId(Long userId) {
        return refreshTokenJpaRepository.findByUserId(userId).orElseThrow(
            () -> new IllegalArgumentException("존재하지 않는 토큰입니다.")); // Todo: custionException
    }

    @Override
    public List<RefreshTokenEntity> findAllByUserId(Long userId) {
        return refreshTokenJpaRepository.findAllByUserId(userId);
    }

    @Override
    public void deleteToken(RefreshTokenEntity token) {
        refreshTokenJpaRepository.delete(token);
    }
}
