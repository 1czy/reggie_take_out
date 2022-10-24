package com.cctv.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cctv.common.R;
import com.cctv.entity.Employee;
import com.cctv.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/login")
    @PostMapping
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
//        log.info("employee is {}",employee);
        return employeeService.login(request,employee);
    }

    @RequestMapping("/logout")
    @PostMapping
    public  R<String> logout(HttpServletRequest request){
        return employeeService.logout(request);
    }

    @RequestMapping("/page")
    @GetMapping
    public  R<Page> page(int page,int pageSize,String name){
        log.info("page is {},pageSize is {},name is {}",page,pageSize,name);
        long id = Thread.currentThread().getId();
        log.info("当前线程为{}",id);
        return employeeService.page(page,pageSize,name);
    }

    @PostMapping
    public R<String>  save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工 is {} ",employee);
        return employeeService.save(request,employee);
    }

    @GetMapping("/{id}")
     public R<Employee> getById(@PathVariable Long id){
        log.info("id is {}",id);

        Employee employee = employeeService.getById(id);
        if(employee == null){
            return R.error("查无此人");
        }
        return R.success(employee);
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee){
        return  employeeService.update(employee);
    }

}
