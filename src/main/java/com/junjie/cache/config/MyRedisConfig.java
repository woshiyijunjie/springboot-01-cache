package com.junjie.cache.config;

import com.junjie.cache.bean.Employee;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.net.UnknownHostException;

@Configuration
public class MyRedisConfig {

    @Bean
    public RedisTemplate<Object, Employee> employeeRedisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<Object, Employee> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer<Employee> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Employee.class);
        template.setDefaultSerializer(jackson2JsonRedisSerializer);
        return template;
    }

    //将原本的序列化存储策略改为 Json 存储策略 2.x 版本的做法
    @Bean
    public RedisCacheManager deptCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
            //.entryTtl(Duration.ofMinutes(15))   // 设置缓存过期时间
            .disableCachingNullValues()     // 禁用缓存空值，不缓存null校验
            .serializeValuesWith(
                RedisSerializationContext.
                SerializationPair.
                // 设置CacheManager的值序列化方式为json序列化，可加入@Class属性
                fromSerializer(new GenericJackson2JsonRedisSerializer())
            );
        // 使用RedisCacheConfiguration创建RedisCacheManager
        RedisCacheManager cm = RedisCacheManager.builder(connectionFactory)
            .cacheDefaults(cacheConfiguration ).build();
        return cm;
    }

    @Bean
    @Primary//默认使用这个存储策略(将原本 redis 默认存储策略作为存储策略)
    public RedisCacheManager myCacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.create(connectionFactory);
    }

}
