package com.gateway.npay.config;

import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheFallbackConfig {
    @Bean
    public CacheErrorHandler cacheErrorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException ex, Cache cache, Object key) {
                System.out.println("Failed to fetch redis down may be");
            }

            @Override
            public void handleCachePutError(RuntimeException ex, Cache cache, Object key, Object value) {

            }

            @Override
            public void handleCacheEvictError(RuntimeException ex, Cache cache, Object key) {

            }

            @Override
            public void handleCacheClearError(RuntimeException ex, Cache cache) {

            }
        };
        }
}
