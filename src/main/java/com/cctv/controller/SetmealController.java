package com.cctv.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cctv.common.R;
import com.cctv.dto.SetmealDto;
import com.cctv.entity.Category;
import com.cctv.entity.Setmeal;
import com.cctv.service.CategoryService;
import com.cctv.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 套餐管理
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("插入成功");
    }

    /**
     * 分页查询套餐
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page is {},pageSize is {},name is {}",page ,pageSize, name);
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotBlank(name),Setmeal::getName,name).orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,queryWrapper);
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");
        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> dtoList = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                setmealDto.setCategoryName(category.getName());
            }
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(dtoList);
        return R.success(setmealDtoPage);
    }

    @DeleteMapping
    public R<String> remove(@RequestParam List<Long> ids){
        log.info("ids is {}", ids);
        setmealService.removeWithDish(ids);
        return  R.success("删除成功");
    }

    /**
     * 查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> select(@PathVariable Long id){
        log.info("id is {}",id);
        SetmealDto setmealDto = setmealService.selectById(id);
        return R.success(setmealDto);
    }

    /**
     * 更新套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.update(setmealDto);
        return R.success("更新成功");
    }

    /**
     * 停售起售
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status,@RequestParam List<Long> ids){
        log.info("ids is {}",ids);
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Setmeal::getStatus, status).in(Setmeal::getId,ids);
        setmealService.update(updateWrapper);
        return R.success("修改停售起售状态成功");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        log.info("categoryId is {},status is {}",setmeal.getCategoryId(),setmeal.getStatus());
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId, setmeal.getCategoryId())
                .eq(setmeal.getStatus() != null,Setmeal::getStatus, setmeal.getStatus());
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);

    }
}
