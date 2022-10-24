package com.cctv.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cctv.dto.SetmealDto;
import com.cctv.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);

    SetmealDto selectById(Long id);

    void update(SetmealDto setmealDto);
}
