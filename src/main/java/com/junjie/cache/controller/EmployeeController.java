package com.junjie.cache.controller;


import com.junjie.cache.bean.Employee;
import com.junjie.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    //根据 id 查询员工
    @GetMapping("/getEmployeeById/{id}")
    public Employee getEmployeeById(@PathVariable("id") Integer id) {
        return employeeService.getEmployeeById(id);
    }
    //根据 id 号来修改该员工
    @GetMapping("/updateEmployeeById/{id}")
    public Employee updateEmployeeById(@PathVariable Integer id,
                                       @RequestParam("lastName") String lastName) {
        Employee employee = new Employee();
        employee.setId(id);
        employee.setLastName(lastName);
        return employeeService.updateEmployeeById(employee);
    }
    //根据 id 号来删除员工
    @GetMapping("/deleteEmployeeById")
    public int deleteEmployeeById(@RequestParam("id") Integer id) {
        return employeeService.deleteEmployeeById(id);
    }
    //根据 lastName 来查询员工
    @GetMapping("/getEmployeeByLastName")
    public Employee getEmployeeByLastName(@RequestParam("lastName") String lastName) {
        return employeeService.getEmployeeByLastName(lastName);
    }
}
