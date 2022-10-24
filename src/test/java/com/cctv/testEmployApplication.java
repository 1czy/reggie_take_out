package com.cctv;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cctv.entity.Employee;
import com.cctv.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@Slf4j
@SpringBootTest
public class testEmployApplication {

    @Autowired
    private EmployeeService employeeService;

    @Test
    void testEmployee(){
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getIdNumber,"111111111111110");
        Employee one = employeeService.getOne(queryWrapper);
        System.out.println(one);
    }

    @Test
    void testUtil(){
        boolean notBlank = StringUtils.isNotBlank(" ");
        boolean notEmpty = StringUtils.isNotEmpty(" ");
        System.out.println(notBlank);
        System.out.println(notEmpty);
        String sub = "abc.tet".substring(0,3);
        System.out.println(sub);
        String ids = "123456123456,12345612345,1211111111";
        String[] split = ids.split(",");
        for(String s:split){
            Integer i = Integer.parseInt(s);
            System.out.println(s);
        }

    }
    @Test
    public void testRamdUtil(){
        int i = RandomUtils.nextInt(6);
        log.info("i is {}",i);

    }
}
