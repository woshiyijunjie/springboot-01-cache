package com.junjie.cache.controller;

import com.junjie.cache.bean.Department;
import com.junjie.cache.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/getDepartmentById/{id}")
    public Department getDepartmentById(@PathVariable("id") Integer id) {
        return departmentService.getDepartmentById(id);
    }

}
