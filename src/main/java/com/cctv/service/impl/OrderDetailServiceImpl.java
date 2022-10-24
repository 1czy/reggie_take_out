package com.cctv.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cctv.dao.OrderDetailMapper;
import com.cctv.entity.OrderDetail;
import com.cctv.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}