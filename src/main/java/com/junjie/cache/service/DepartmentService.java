package com.junjie.cache.service;

import com.junjie.cache.bean.Department;
import com.junjie.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

@CacheConfig(cacheNames = "dept", cacheManager = "deptCacheManager")
@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Qualifier("deptCacheManager")//按照 id 名字匹配bean 来确定缓存管理器
    @Autowired
    private RedisCacheManager redisCacheManager;

    @Cacheable
    public Department getDepartmentById(Integer id) {

        //获取某个缓存
        Cache dept = redisCacheManager.getCache("dept");
        dept.put("key", departmentMapper.getDepartmentById(id));

        return departmentMapper.getDepartmentById(id);
    }

}
