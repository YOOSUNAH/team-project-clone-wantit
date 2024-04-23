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

@Slf4j
@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;


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
    public RedisTemplate<String, AuctionItemPageableResponseDto> auctionItemRedisTemplate() {

        RedisTemplate<String, AuctionItemPageableResponseDto> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());

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
        RedisSerializer<?> valueSerializer = auctionItemRedisTemplate.getValueSerializer();
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration
            .defaultCacheConfig()
            .serializeKeysWith(SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(SerializationPair.fromSerializer(valueSerializer))
            .entryTtl(Duration.ofMinutes(30L));

        Map<String, RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();
        redisCacheConfigurationMap.put(
            "auctionItemCache",
            redisCacheConfiguration.entryTtl(Duration.ofMinutes(5))
        );
        log.info("레디스 캐싱 로그" + "Redis Cache Configuration: {}", redisCacheConfigurationMap);

        return RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory())
            .withInitialCacheConfigurations(redisCacheConfigurationMap)
            .cacheDefaults(redisCacheConfiguration)
            .build();
    }
}
