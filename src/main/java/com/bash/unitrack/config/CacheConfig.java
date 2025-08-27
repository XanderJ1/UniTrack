package com.bash.unitrack.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Bean
    public Caffeine<Object, Object> caffeineConfig(){
        return Caffeine.newBuilder()
                .initialCapacity(50)
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.HOURS)
                .expireAfterWrite(30, TimeUnit.MINUTES);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
