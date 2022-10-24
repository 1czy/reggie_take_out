package com.cctv.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cctv.common.CustomException;
import com.cctv.dao.DishMapper;
import com.cctv.dto.DishDto;
import com.cctv.entity.Dish;
import com.cctv.entity.DishFlavor;
import com.cctv.service.DishFlavorService;
import com.cctv.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long dishId = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(
                (item) -> {item.setDishId(dishId);
                return item;}).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 查找菜品
     * @param id
     * @return
     */
    @Override
    public DishDto selectWithFlavor(Long id) {
        DishDto dishDto = new DishDto();
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId,id);
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 更新内容
     * @param dishDto
     */
    @Override
    @Transactional
    public void update(DishDto dishDto) {
        this.removeById(dishDto.getId());
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);
        this.saveWithFlavor(dishDto);
    }

    /**
     * 删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void removeDish(List<Long> ids) {
        LambdaQueryWrapper<Dish> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId,ids).eq(Dish::getStatus, 1);
        int count = this.count(queryWrapper);
        if(count > 0) throw new CustomException("未停售商品不能删除");
        this.removeByIds(ids);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.in(DishFlavor::getDishId, ids);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
    }

}
