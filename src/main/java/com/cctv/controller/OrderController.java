package com.cctv.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cctv.common.BaseContext;
import com.cctv.common.R;
import com.cctv.entity.OrderDetail;
import com.cctv.entity.Orders;
import com.cctv.service.OrderDetailService;
import com.cctv.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDetailService orderDetailService;

    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        orderService.submit(orders);
        return R.success("结算成功");
    }
    @GetMapping("/userPage")
    public R<Page> userPage(int page,int pageSize){
        Page<OrderDetail> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        List<Orders> list = orderService.list(queryWrapper);
        List<Long> ids = list.stream().map((item) -> {
            return item.getId();
        }).collect(Collectors.toList());
        LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(OrderDetail::getOrderId,ids);
        orderDetailService.page(pageInfo,queryWrapper1);
        return R.success(pageInfo);
    }
}
