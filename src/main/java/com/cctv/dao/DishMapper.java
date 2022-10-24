package com.cctv.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cctv.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
