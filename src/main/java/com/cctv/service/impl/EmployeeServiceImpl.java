package com.cctv.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cctv.common.R;
import com.cctv.dao.EmployeeMapper;
import com.cctv.entity.Employee;
import com.cctv.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    /**
     * 登录
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        //md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee one = this.getOne(queryWrapper);
        if(one == null){
            return R.error("用户名不存在");
        }
        if(!one.getPassword().equals(password)){
            return  R.error("密码错误");
        }
        if(one.getStatus() == 0){
            return R.error("该账号禁用中");
        }
        request.getSession().setAttribute("employee",one);
        return R.success(one);
    }
    /**
     * 登出
     * @param request
     * @return
     */
    @Override
    public R<String> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.removeAttribute("employee");
            session.invalidate();
        }
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param request
     * @param employee
     * @return
     */
    @Override
    public R<String> save(HttpServletRequest request, Employee employee) {
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
/*        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());*/
        HttpSession session = request.getSession(false);
        if(session == null){
            return R.error("登录重试");
        }
      /*  Employee employee1 = (Employee) session.getAttribute("employee");
        Long empId = employee1.getId();
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);*/
        this.save(employee);
        return R.success("新增成功");
    }

    /**
     * 分页查询员工
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> page(int page, int pageSize, String name) {

        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        this.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

    @Override
    public R<String> update( Employee employee) {
        log.info("employee is {}",employee);
//        HttpSession session = request.getSession(false);
/*        Employee employee1 = (Employee) session.getAttribute("employee");
        Long id = employee1.getId();
        employee.setUpdateUser(id);
        employee.setUpdateTime(LocalDateTime.now());*/
        long id = Thread.currentThread().getId();
        log.info("线程id为：{}",id);
        this.updateById(employee);
        return R.success("更新成功");
    }

}
