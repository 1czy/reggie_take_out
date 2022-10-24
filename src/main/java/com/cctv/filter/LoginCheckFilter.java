package com.cctv.filter;

import com.alibaba.fastjson.JSON;
import com.cctv.common.BaseContext;
import com.cctv.common.R;
import com.cctv.entity.Employee;
import com.cctv.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 检查用户是否完成登录
 */
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        log.info("过滤器启动中");
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String requestURI = request.getRequestURI();
        String[] urls = {"/employee/login", "/employee/logout", "/backend/**",
                "/front/**", "/backend/page/demo/upload.html","/user/sendMsg",
                "/user/login"};
        boolean check = check(urls, requestURI);
        if (check) {
            filterChain.doFilter(request, response);
            return;
        }
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("employee") != null){
            long id = Thread.currentThread().getId();
            log.info("过滤线程为{}", id);
            Employee employee = (Employee)session.getAttribute("employee");
            Long empId = employee.getId();
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        if(session != null && session.getAttribute("user") != null){
            long id = Thread.currentThread().getId();
            log.info("过滤线程为{}", id);
            User user = (User)session.getAttribute("user");
            Long userId = user.getId();
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request,response);
            return;
        }
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    private boolean check(String[] urls, String requestURI) {
        boolean match;
        for (String url : urls) {
            match = antPathMatcher.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }
}
