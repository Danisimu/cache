package com.example.simplerestapi.configuration;

import com.example.simplerestapi.configuration.properties.AppCacheProperties;
import com.google.common.cache.CacheBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
@EnableConfigurationProperties(AppCacheProperties.class)
public class CacheConfiguration {

    @Bean
    @ConditionalOnExpression("'${app.cache.cacheTypes}'.equals('inMemory')")
    public ConcurrentMapCacheManager inMemoryCacheManager(AppCacheProperties properties){
        var cacheManager = new ConcurrentMapCacheManager(){
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(properties.getCaches().get(name).getExpiry())
                                .build()
                                .asMap(),
                        true
                );
            }
        };
        var cacheNames = properties.getCacheNames();

        if (!cacheNames.isEmpty()){
            cacheManager.setCacheNames(cacheNames);
        }

        return cacheManager;
    }

    @Bean
    @ConditionalOnProperty(prefix = "app.redis", name = "enable", havingValue = "true")
    @ConditionalOnExpression("'${app.cache.cacheTypes}'.equals('redis')")
    public CacheManager redisCacheManager(AppCacheProperties appCacheProperties, LettuceConnectionFactory lettuceConnectionFactory){
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig();
        Map<String,RedisCacheConfiguration> redisCacheConfigurationMap = new HashMap<>();

        appCacheProperties.getCacheNames().forEach(cacheName -> {
            redisCacheConfigurationMap.put(cacheName, RedisCacheConfiguration.defaultCacheConfig().entryTtl(
                    appCacheProperties.getCaches().get(cacheName).getExpiry()
            ));
        });

        return RedisCacheManager.builder(lettuceConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(redisCacheConfigurationMap)
                .build();
    }
}
