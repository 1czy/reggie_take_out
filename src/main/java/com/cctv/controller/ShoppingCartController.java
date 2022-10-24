package com.cctv.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cctv.common.BaseContext;
import com.cctv.common.R;
import com.cctv.entity.ShoppingCart;
import com.cctv.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 加入购物车
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("shoppingCart is {}",shoppingCart);
        log.info(" main shoppingCart is {}",shoppingCart);
        //获取当前用户用户id
        Long id = BaseContext.getCurrentId();
        //查询shoppingCart 表 where user_id =id and dish_flavor = shoppingCart.getDishFlavor;
        //
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, id).eq(shoppingCart.getDishFlavor() != null,
                ShoppingCart::getDishFlavor,shoppingCart.getDishFlavor());
        queryWrapper.eq(ShoppingCart::getName,shoppingCart.getName());
        ShoppingCart one = shoppingCartService.getOne(queryWrapper);
        if(one != null){
            //存在 update num+1
            one.setNumber(one.getNumber()+1);
            shoppingCartService.updateById(one);
            return R.success(shoppingCart);
        }
        //否则插入
        shoppingCart.setUserId(id);
        shoppingCart.setNumber(1);
        shoppingCart.setCreateTime(LocalDateTime.now());
        shoppingCartService.save(shoppingCart);
        return R.success(shoppingCart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,id);
        queryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空成功");
    }

    @PostMapping("/sub")
    public R<String> sub(@RequestBody ShoppingCart shoppingCart){
        log.info("dish is {},setmeal is {}",shoppingCart.getDishId(),shoppingCart.getSetmealId());
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(shoppingCart.getDishId()!= null,ShoppingCart::getDishId,shoppingCart.getDishId());
        queryWrapper.eq(shoppingCart.getSetmealId()!=null,ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        ShoppingCart shoppingCartOne = shoppingCartService.getOne(queryWrapper);
        Integer number = shoppingCartOne.getNumber();
        if(number == 1)
            shoppingCartService.remove(queryWrapper);
        shoppingCartOne.setNumber(number - 1);
        shoppingCartService.updateById(shoppingCartOne);
        return R.success("删除成功");
    }
}
