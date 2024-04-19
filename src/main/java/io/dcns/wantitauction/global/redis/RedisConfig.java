package io.dcns.wantitauction.global.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dcns.wantitauction.domain.auctionItem.dto.AuctionItemPageableResponseDto;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Slf4j
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    private final Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

    public RedisConfig(Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder) {
        this.jackson2ObjectMapperBuilder = jackson2ObjectMapperBuilder;
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisHost, redisPort);

    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    @Bean
    @Qualifier("auctionItemRedisTemplate")
    public RedisTemplate<String, AuctionItemPageableResponseDto> auctionItemRedisTemplate(
        RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, AuctionItemPageableResponseDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // localdateTijme 오류를 해결하기 위해 추가
        Jackson2JsonRedisSerializer<AuctionItemPageableResponseDto> serializer
            = new Jackson2JsonRedisSerializer<>(AuctionItemPageableResponseDto.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        serializer.setObjectMapper(objectMapper);

        redisTemplate.setValueSerializer(serializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    @Qualifier("redisTemplate")
    public ValueOperations<String, String> valueOperations(
        RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    @Bean
    public RedisCacheManager redisCacheManager(
        @Qualifier("auctionItemRedisTemplate") RedisTemplate<String, AuctionItemPageableResponseDto> auctionItemRedisTemplate
    ) {

        // auctionItemRedisTemplate와 연결하기 위해 추가
        RedisSerializer<?> valueSerializer = auctionItemRedisTemplate.getValueSerializer();

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(
            Object.class); // Jackson2JsonRedisSerializer를 사용하도록 변경
        serializer.setObjectMapper(jackson2ObjectMapperBuilder.modules(new JavaTimeModule())
            .build()); // Java 8의 날짜 및 시간 형식을 처리할 수 있도록 JavaTimeModule이 등록된 ObjectMapper를 사용

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
            .defaultCacheConfig()
            .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(SerializationPair.fromSerializer(valueSerializer))  //위에서 설정한 Jackson2JsonRedisSerializer를 사용해, JSON으로 직렬화되어 Redis에 저장
            .entryTtl(Duration.ofMinutes(30L));

        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put("auctionItemCache",
            redisCacheConfiguration.entryTtl(Duration.ofMinutes(5)));

        log.info("레디스 캐싱 로그" + "Redis Cache Configuration: {}", redisCacheConfigurationMap);

        return RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory())
            .withInitialCacheConfigurations(redisCacheConfigurationMap)
            .cacheDefaults(redisCacheConfiguration)
            .build();
    }
}
