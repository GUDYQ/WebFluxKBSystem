package org.example.webfluxlearning.base.cache;

import java.time.Duration;

public final class CacheNames {

    // 默认策略：5分钟本地 + 6分钟 Redis
    public static final String DEFAULT = "default";

    // 特殊缓存：显式声明策略
    public static final CacheSpec BOOKS = new CacheSpec("books", Duration.ofMinutes(10), Duration.ofMinutes(12));
    public static final CacheSpec USERS = new CacheSpec("users", Duration.ofHours(1), Duration.ofHours(1).plusMinutes(5));

    // 内部类：描述缓存策略
    public static class CacheSpec {
        public final String name;
        public final Duration localTtl;
        public final Duration remoteTtl;
        public final long maxSize;

        public CacheSpec(String name, Duration localTtl, Duration remoteTtl) {
            this(name, localTtl, remoteTtl, 1000);
        }
        public CacheSpec(String name, Duration localTtl, Duration remoteTtl, long maxSize) {
            this.name = name;
            this.localTtl = localTtl;
            this.remoteTtl = remoteTtl;
            this.maxSize = maxSize;
        }
    }
}
