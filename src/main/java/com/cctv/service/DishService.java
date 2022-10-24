package com.cctv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cctv.dto.DishDto;
import com.cctv.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto selectWithFlavor(Long id);

    void update(DishDto dishDto);

    void removeDish(List<Long> id);
}
