package io.dcns.wantitauction.global.redis;

import java.time.Duration;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    // redis client
    private final StringRedisTemplate template;  // String으로만 값을 주고 받기때문에 의존성 주입해서 활용

    public String getData(String key) {  // key값으로 value가져오기
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    public boolean existData(String key) {  // key값으로 데이터가 존재하는지 확인
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    public void setDataExpire(String key,
                              String value,
                              long duration) { // key, value 저장 + 만료시간 설정
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {  //key값으로 데이터 삭제
        template.delete(key);
    }
}
