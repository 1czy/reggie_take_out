package com.cctv.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cctv.dao.ShoppingCartMapper;
import com.cctv.entity.ShoppingCart;
import com.cctv.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
