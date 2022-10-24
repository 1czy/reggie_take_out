package com.cctv.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cctv.dao.DishFlavorMapper;
import com.cctv.entity.DishFlavor;
import com.cctv.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
