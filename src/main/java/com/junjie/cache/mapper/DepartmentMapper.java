package com.junjie.cache.mapper;

import com.junjie.cache.bean.Department;

public interface DepartmentMapper {

    //根据 id 查询 department
    Department getDepartmentById(Integer id);

}
