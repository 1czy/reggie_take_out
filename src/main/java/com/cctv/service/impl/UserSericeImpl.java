package com.cctv.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cctv.dao.UserMapper;
import com.cctv.entity.User;
import com.cctv.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserSericeImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
