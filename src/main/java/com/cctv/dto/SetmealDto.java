package com.cctv.dto;


import com.cctv.entity.Setmeal;
import com.cctv.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
