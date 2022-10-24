package com.cctv.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cctv.common.R;
import com.cctv.entity.User;
import com.cctv.service.UserService;
import com.cctv.util.SMSUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     *
     * @param request
     * @param phone
     */
    @PostMapping("/sendMsg")
    public void sendMsg(HttpServletRequest request, String phone) {
        if (StringUtils.isBlank(phone))
            return;
        log.info("phone is {}", phone);
        String code = RandomUtil.randomNumbers(6);
        SMSUtil.sendSMS(request,phone,code);
        log.info("code is {}", code);
        HttpSession session = request.getSession();
        session.setAttribute(phone, code);
    }

    /**
     * 登录验证
     * @param request
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody Map<String,String> map) {
        log.info("phone is {}, code is {}", map.get("phone"), map.get("code"));
        HttpSession session = request.getSession(false);
        String code = (String) session.getAttribute(map.get("phone"));
        if (code == null || !code.equals(map.get("code")))
            return R.error("验证码错误");
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, map.get("phone"));
        User one = userService.getOne(queryWrapper);
        if (one == null ) {
            one = new User();
            one.setPhone(map.get("phone"));
            one.setStatus(1);
            one.setName("user_" + RandomUtil.randomString(4));
            userService.save(one);
        }
        if(one.getStatus() != 1)
            return R.error("账号禁用中");
        session.removeAttribute(map.get("phone"));
        session.setAttribute("user", one);
        return R.success(one);
    }
    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request){

        HttpSession session = request.getSession(false);
        if(session != null){
            session.removeAttribute("user");
            session.invalidate();
        }
        return R.success("退出成功");
    }
}