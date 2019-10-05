package com.dark.service;

import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.pojo.Setmeal;

public interface SetmealService {
    void add(Integer[] checkgroupIds, Setmeal setmeal);

    PageResult findByPage(QueryPageBean queryPageBean);

    void deleteById(Integer id);
}
