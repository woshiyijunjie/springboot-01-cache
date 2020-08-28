package com.junjie.cache.service;

import com.junjie.cache.bean.Employee;
import com.junjie.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;

//在这里面配置以后后面的缓存名称都可以不用写了
@CacheConfig(cacheNames = "emp")
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public List<Employee> getAllEmployee() {
        return employeeMapper.getAllEmployee();
    }
    /*
     * 将方法运行的结果进行缓存，以后有相同的数据就直接从缓存中获取不用重新访问数据库
     *
     * cacheNames/value：指定将方法返回的数据放到哪个缓存中(可以放多个，数组的方式)
     *
     * key 缓存数据使用 key: 可以用他来指定，默认使用方法参数的值
     *      编写 SpEL: #id 是参数 id 的值 #a0 #p0 #root.args[0]
     *
     * keyGenerator: key 的生成器，可以自己指定 key 的生成器的组件 id
     * 其中 key/keyGenerator 二选一指定
     * eg: key = "#root.methodName+ '['+ #id + ']'"
     *     keyGenerator = "myKeyGenerator" 使用自定义的 key 的生成策略
     *
     * cacheManager: 指定缓存管理器
     * 管理多个 cache 组件，对缓存的真正 CRUD 操作在缓存 cache 组件中，每一个缓存
     * 都有自己唯一的一个名字
     *
     * condition: 指定符合条件的情况下缓存
     *      , condition = "#id > 0"
     *
     * unless: 否定缓存，当 unless 指定条件为 true , 方法的返回值就不会缓存，可以获取到结果进行判断
     *      , unless = "#result == null"
     *
     * sync:是否使用异步模式
     *
     * 原理
        1.自动配置 CacheAutoConfiguration.java 类
        2.配置组件类
            0 = "org.springframework.boot.autoconfigure.cache.GenericCacheConfiguration"
            1 = "org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration"
            2 = "org.springframework.boot.autoconfigure.cache.EhCacheCacheConfiguration"
            3 = "org.springframework.boot.autoconfigure.cache.HazelcastCacheConfiguration"
            4 = "org.springframework.boot.autoconfigure.cache.InfinispanCacheConfiguration"
            5 = "org.springframework.boot.autoconfigure.cache.CouchbaseCacheConfiguration"
            6 = "org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration"
            7 = "org.springframework.boot.autoconfigure.cache.CaffeineCacheConfiguration"
            8 = "org.springframework.boot.autoconfigure.cache.SimpleCacheConfiguration" [默认开启]
            9 = "org.springframework.boot.autoconfigure.cache.NoOpCacheConfiguration"
      *  3.那个配置类默认生效 SimpleConfiguration
      *  4.给容器注册一个 CacheManager : ConcurrentMapCacheManager
      *  5.可以创建 ConcurrentMapCache 类型的缓存组件，他的作用是将数据保存在 ConcurrentMap 中
      *
      *  运行流程
      *  1.在方法运行之前先去查询 Cache 缓存组件，按照 cacheNames 指定的名字获取
      *     (CacheManager先获取组件的缓存)，第一次获取缓存如果没有Cache 组件会自动创建
      *  2.去 cache 中查找缓存的内容，使用一个 key， 默认是方法的参数
      *     key 是按照某种策略生成的：默认是 keyGenerator 生成的， 默认使用 SimpleKeyGenerator 生成 key
      *     如果没有参数：key = new SimpleKey()
      *     如果只有一个参数：key = 参数的值
      *     如果有多个参数: key = new SimpleKey(params)
      *  3.没有缓存就调用目标方法
      *  4.将目标方法的返回结果，放入缓存中
      *
      *  @Cacheable 标注的方法执行前先检查缓存中有没有这个数据，默认按照参数作为 key 值取查询缓存
      *  先看缓存有没有结果，没有的话再调用方法
      * */
    @Cacheable(value = {"emp"})
    public Employee getEmployeeById(Integer id) {
        System.out.println("查询 " + id + " 号员工");
        return employeeMapper.getEmployeeById(id);
    }

    /*
    * @CachePut 既调用了方法又更新了缓存(修改了数据库的某个应用又要修改缓存)
    * 先调用方法，再把结果放入缓存中
    * 测试
    * 1.查询一号员工
    * 2.以后查询还是之前的结果
    * 3.更新一号员工
    *   将方法的返回值也放进返回了
    *   key: 传入的 employee 对象值，返回 employee 对象
    * 4.查询员工
    *   应该是更新后的员工，为什么没有更新？
    *   key 值不相同无法更新之前缓存中的数据，他只会重新建立缓存数据
    *   key = "#employee.id" 使用传入参数的员工 id
    *   key = "#result.id" 注意: @Cacheable 注解是不能够这样用的，因为他在方法运行之前就会在缓存中进行检测
    * */
    @CachePut(value = "emp", key = "#employee.id")
    public Employee updateEmployeeById(Employee employee) {
        System.out.println("更新员工" + employee);
        employeeMapper.updateEmployeeById(employee);
        //方法返回数据会被放入缓存，这里再将数据查全
        return employeeMapper.getEmployeeById(employee.getId());
    }
    /*
    * @CacheEvict 清空缓存
    *   key: 指定要清除的数据
    *   allEntries = false 是否删除掉 emp 缓存中所有数据(默认是 false)
    *   beforeInvocation 在方法执行之前是否清除缓存，默认是 false
    * */
    //根据 id 来删除员工
    @CacheEvict(value = "emp")
    public int deleteEmployeeById(Integer id) {
        //return employeeMapper.deleteEmployeeById(id);
        System.out.println(id);
        return 1;
    }
    /*
    * @Caching 注解可以指定上面三种注解,主要是用来定义复杂的缓存规则
    * */
    //根据用户名来查询信息
    @Caching(
        cacheable = {
            @Cacheable(value = "emp", key = "#lastName")
        },
        put = {
            @CachePut(value = "emp", key = "#result.id"),
            @CachePut(value = "emp", key = "#result.email")
        }
    )
    public Employee getEmployeeByLastName(String lastName) {
        return employeeMapper.getEmployeeByLastName(lastName);
    }

}
