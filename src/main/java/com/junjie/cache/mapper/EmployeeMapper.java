package com.junjie.cache.mapper;

import com.junjie.cache.bean.Employee;

import java.util.List;

public interface EmployeeMapper {

    //查询
    List<Employee> getAllEmployee();
    //根据 id 查询
    Employee getEmployeeById(Integer id);
    //根据 lastName 进行查询
    Employee getEmployeeByLastName(String lastName);
    //更新员工
    int updateEmployeeById(Employee employee);
    //删除员工
    int deleteEmployeeById(Integer id);

}
