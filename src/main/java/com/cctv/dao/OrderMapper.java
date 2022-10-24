package com.cctv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cctv.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Orders> {

}