package io.dcns.wantitauction.global.jwt;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class RefreshTokenRepository {

    private static final String TOKEN_PREFIX = "token_";  // 식별하기 편하게 하기 위해서, redis에 저장할때 사용한다.

    private final RedisTemplate<String, String> redisTemplate;
    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    public void delete(final String subject) {
        redisTemplate.delete(TOKEN_PREFIX + subject);
    }

    public void save(final Long userId, final String refreshToken) {
        valueOperations.set(TOKEN_PREFIX + userId, refreshToken);
        redisTemplate.expire(TOKEN_PREFIX + userId, 7L, TimeUnit.DAYS);
    }

    public boolean existsByUserId(Long userId) {
        return Boolean.TRUE.equals(valueOperations.getOperations().hasKey(TOKEN_PREFIX + userId));
    }

    public String findByUserId(Long userId) {
        return valueOperations.get(TOKEN_PREFIX + userId);
    }
}
