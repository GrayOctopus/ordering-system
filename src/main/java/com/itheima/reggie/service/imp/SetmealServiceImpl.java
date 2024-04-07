package com.itheima.reggie.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;


    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        // 更新 setmeal 表
        this.save(setmealDto);
        // 更新 setmealDish 表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }


    /**
     * 删除套餐
     * @param ids
     */
    @Override
    public void deleteWithDish(List<Long> ids) {
        // 删除 setmeal 表中的数据
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.in(Setmeal::getId,ids);
        setmealLambdaQueryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(setmealLambdaQueryWrapper);

        if (count > 0) {
            throw new CustomException("正在出售，无法删除");
        }
        this.removeByIds(ids);
        // 删除 setmealDish 表中的数据

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);
    }

    @Override
    public void updateStatusByIds(Integer status, List<Long> ids) {
        ArrayList<Setmeal> setmealArrayList = new ArrayList<>();
        for (Long id : ids) {
            Setmeal setmeal = this.getById(id);
            setmeal.setStatus(status);
            setmealArrayList.add(setmeal);
        }
        this.updateBatchById(setmealArrayList);
    }
}
