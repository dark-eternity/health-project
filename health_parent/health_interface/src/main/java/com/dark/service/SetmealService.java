package com.dark.service;

import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.pojo.Setmeal;

import java.util.List;

public interface SetmealService {
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findByPage(QueryPageBean queryPageBean);

    void deleteById(Integer id);

    List<Integer> findMealAnnoGroup(Integer id);

    void update(Integer[] checkgroupIds, Setmeal setmeal);

    Setmeal findById(Integer id);
}
