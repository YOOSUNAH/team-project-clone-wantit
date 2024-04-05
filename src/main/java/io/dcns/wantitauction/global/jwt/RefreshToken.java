package io.dcns.wantitauction.global.jwt;

import jakarta.persistence.Id;
import java.util.concurrent.TimeUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "jwtToken", timeToLive = 60 * 60)
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

        @Id
        private Long id;

        @Indexed
        private String accessToken;

        private String refreshToken;

        @TimeToLive(unit = TimeUnit.MILLISECONDS) // 디폴트는 TimeUnit.SECONDS
        private long expiration;

}
