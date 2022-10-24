package com.cctv.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cctv.common.CustomException;
import com.cctv.common.R;
import com.cctv.dao.CategoryMapper;
import com.cctv.entity.Category;
import com.cctv.entity.Dish;
import com.cctv.entity.Setmeal;
import com.cctv.service.CategoryService;
import com.cctv.service.DishService;
import com.cctv.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private DishService dishService;

    @Override
    public R<Page> selectpage(int page, int pageSize) {

        Page<Category> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        this.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 删除菜品或套餐
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithId(Long ids) {
        Category category = this.getById(ids);
        //查type
        //type = 1  dish表 dish_id = id 是否为null
        if(category.getType() == 1){
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Dish::getCategoryId,ids);
            List<Dish> list = dishService.list(queryWrapper);
            if(list.size() != 0) throw new CustomException("菜品类型关联了菜品不能删除！！！");
        }
        if(category.getType() == 2) {
            LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Setmeal::getCategoryId, ids);
            List<Setmeal> list = setmealService.list(queryWrapper);
            if (list.size() != 0) throw new CustomException("套餐类型关联了菜品不能删除！！！");
        }
        //删除成功;
        this.removeById(ids);
    }
}
