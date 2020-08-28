package com.junjie.cache;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/*
* cacheManager 在没有其他配置指定的时候默认是使用 ConcurrentMapCacheManager 作为缓存的，
* 将数据保存在  ConcurrentMap<Object, Object> 中
* 开发的过程中往往使用的是缓存中间件：eg: redis, memcached, ehcache
*
* 整合 redis
* 1.安装 redis 使用 docker
* 2.引入 redis 的 starter
* 3.配置 redis
*
* 原理: CacheManager == Cache 缓存组件实际给缓存中存取数据
* 1.引入 redis 的 starter ，容器中保持的是 RedisCacheManager
* 2.RedisCacheManager 帮我们创建 RedisCache 来作为组件，RedisCache 通过操作 redis 来缓存数据
* 3.默认保存数据 k-v 都是 Object，利用序列化来保存的
* 	(1),引入了 redis 的 starter, cacheManager 变成 redisCacheManager
* 	(2),默认创建了 RedisCacheManager, 操作redis 时使用的是 RedisTemplate<Object, Object>
* 	(3),RedisTemplate<Object, Object> 默认使用的是 jdk 的序列化机制
* 4,自定义 cacheManager
* */

@MapperScan(value = "com.junjie.cache.mapper")
@SpringBootApplication
@EnableCaching//开启基于注解的缓存
public class Springboot01CacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(Springboot01CacheApplication.class, args);
	}

}
