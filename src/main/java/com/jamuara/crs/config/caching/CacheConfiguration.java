package com.jamuara.crs.config.caching;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfiguration {
//    @Bean
//    public CacheManager cacheManager() {
//        return new ConcurrentMapCacheManager("activities");
//    }

    @Bean
    public CacheManager cacheManagerSetup() {
//        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager("locations", "flightOffer");
//        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
//                .expireAfterWrite(10, TimeUnit.MINUTES)
//                .maximumSize(1000));
//        return caffeineCacheManager;
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager() {
            @Override
            protected @NonNull Cache<Object, Object> createNativeCaffeineCache(@NonNull String name) {
                if("locations".equals(name)) {
                    return Caffeine.newBuilder()
                            .expireAfterWrite(10, TimeUnit.MINUTES)
                            .maximumSize(1000)
                            .build();
                } else if("flightOffers".equals(name)) {
                    return Caffeine.newBuilder()
                            .expireAfterWrite(5, TimeUnit.MINUTES)
                            .maximumSize(100)
                            .build();
                } else if("activities".equals(name)) {
                    return Caffeine.newBuilder()
                            .expireAfterWrite(12, TimeUnit.HOURS)
                            .maximumSize(100)
                            .build();
                }

                return Caffeine.newBuilder()
                        .expireAfterAccess(10, TimeUnit.MINUTES)
                        .maximumSize(1000)
                        .build();
            }
        };

        caffeineCacheManager.setCacheNames(List.of("locations", "flightOffers", "activities"));

        return caffeineCacheManager;
    }
}
