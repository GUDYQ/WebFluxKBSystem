package org.example.webfluxlearning.utils;

import com.github.benmanes.caffeine.cache.AsyncCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.example.webfluxlearning.base.cache.CacheNames;
import org.example.webfluxlearning.base.exception.ai.AiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheUtil {
    @Autowired
    private ReactiveRedisOperations<String, Object> operations;
    private final Map<String, AsyncCache<String, Object>> caches = new ConcurrentHashMap<>();
    private static final Duration DEFAULT_LOCAL_TTL = Duration.ofMinutes(5);
    private static final Duration DEFAULT_REMOTE_TTL = Duration.ofMinutes(6);
    private static final long DEFAULT_MAX_SIZE = 1000;

    public Mono<Boolean> set(String key, Object value) {
        return operations.opsForValue().set(key, value);
    }

    public Mono<Object> get(String key) {
        return operations.opsForValue().get(key);
    }

    public Mono<Boolean> setExpire(String key, Object value, Long expire) {
        return operations.opsForValue().set(key, value, Duration.of(expire, ChronoUnit.SECONDS));
    }
    public Mono<Long> delete(String key) {
        return operations.delete(key);
    }

    public <T> Mono<T> get(CacheNames.CacheSpec spec, String key, Class<T> type, Mono<T> loader) {
        return getInternal(spec.name, key, type, loader, spec.localTtl, spec.remoteTtl, spec.maxSize);
    }

    public <T> Mono<T> get(String cacheName, String key, Class<T> type, Mono<T> loader) {
        return getInternal(cacheName, key, type, loader, DEFAULT_LOCAL_TTL, DEFAULT_REMOTE_TTL, DEFAULT_MAX_SIZE);
    }

    public <T> Mono<T> getInternal(String cacheName, String key, Class<T> type, Mono<T> loader,
                                   Duration localTtl, Duration remoteTtl, long maxSize) {
        AsyncCache<String, Object> localCache = caches.computeIfAbsent(cacheName, n ->
                Caffeine.newBuilder()
                        .maximumSize(maxSize)
                        .expireAfterWrite(localTtl)
                        .buildAsync()
        );

        CompletableFuture<Object> l1Value = localCache.getIfPresent(key);
        if (l1Value != null) {
            return Mono.fromFuture(l1Value).cast(type).switchIfEmpty(Mono.empty());
        }
        return get(key).switchIfEmpty(Mono.empty())
                .cast(type).doOnNext(value -> localCache.put(key, CompletableFuture.completedFuture(value)));
    }

    public Mono<Boolean> put(CacheNames.CacheSpec spec, String key, Object value) {
        return putInternal(spec.name, key, value, spec.localTtl, spec.remoteTtl, spec.maxSize);
    }

    public Mono<Boolean> put(String cacheName, String key, Object value) {
        return putInternal(cacheName, key, value, DEFAULT_LOCAL_TTL, DEFAULT_REMOTE_TTL, DEFAULT_MAX_SIZE);
    }

    public Mono<Boolean> putInternal(String cacheName, String key, Object value, Duration localTtl,
                                     Duration remoteTtl, long maxSize) {
        AsyncCache<String, Object> localCache = caches.computeIfAbsent(cacheName, n ->
                Caffeine.newBuilder()
                        .maximumSize(maxSize)
                        .expireAfterWrite(localTtl)
                        .buildAsync()
        );
        localCache.put(key, CompletableFuture.completedFuture(value));
        return set(key, value);
    }

    public Mono<Long> evict(String cacheName, String key) {
        AsyncCache<String, Object> localCache = caches.get(cacheName);
        if (localCache != null) {
            return Mono.just(1L);
        }
        return delete(key);
    }
}
