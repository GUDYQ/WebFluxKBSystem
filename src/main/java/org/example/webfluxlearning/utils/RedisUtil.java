package org.example.webfluxlearning.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Component
public class RedisUtil {

    private RedisUtil() {
        // Private constructor to prevent instantiation
    }

    @Autowired
    private ReactiveRedisOperations<String, Object> operations;

    public Mono<Boolean> set(String key, Object value) {
        return operations.opsForValue().set(key, value);
    }

    public Mono<Object> get(String key) {
        return operations.opsForValue().get(key);
    }

    public Mono<Boolean> setExpire(String key, Object value, Long expire) {
        return operations.opsForValue().set(key, value, Duration.of(expire, ChronoUnit.SECONDS));
    }
}
