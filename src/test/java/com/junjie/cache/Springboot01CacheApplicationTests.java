package com.junjie.cache;

import com.junjie.cache.bean.Employee;
import com.junjie.cache.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class Springboot01CacheApplicationTests {

	@Autowired
	private EmployeeService employeeService;

	//RedisAutoConfiguration这个类里面加入了 IOC 容器
	@Autowired
	private RedisTemplate redisTemplate;// k - v 对象

	@Autowired
	private RedisTemplate<Object, Employee> employeeRedisTemplate;

	@Autowired
	private StringRedisTemplate stringRedisTemplate;// 操作字符串

	/*
	* 操作 redis 常见的 5 大数据类型
	* String(字符串), List(列表), Set(集合), Hash(散列), ZSet(有序集)
	* stringRedisTemplate.opsForValue() [String 字符串]
	* stringRedisTemplate.opsForList() [操作列表]
	* stringRedisTemplate.opsForSet() [操作集合]
	* stringRedisTemplate.opsForHash() [操作散列]
	* stringRedisTemplate.opsForZSet() [操作有序集]
	* */
	//测试保存 String
	@Test
	void contextLoads() {
		//给 redis 中保存一个数据
		//stringRedisTemplate.opsForValue().append("msg", "hello");
		//String v = stringRedisTemplate.opsForValue().get("k1");
		//System.out.println(v);
		stringRedisTemplate.opsForList().leftPush("myList", "1");
		stringRedisTemplate.opsForList().leftPush("myList", "2");
	}
	//测试保存对象
	@Test
	void contextLoads1() {
		//默认如果保存对象，使用 jdk 序列化机制，序列化后的数据保存到 redis 中
		//redisTemplate.opsForValue().set("emp-01",employeeService.getEmployeeById(1));
		employeeRedisTemplate.opsForValue().set("emp-01",employeeService.getEmployeeById(1));
		/*1.将数据以Json 的方式保存
		* (1) 自己将对象转化为 Json
		* (2) redisTemplate 有默认的序列化规则 改变见 config
		* */
	}

}
