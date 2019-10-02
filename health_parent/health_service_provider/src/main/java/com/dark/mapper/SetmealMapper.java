package com.dark.mapper;

import com.dark.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealMapper {
    void add(Setmeal setmeal);

    void addMealAnnoGroup(List<Map<String, Object>> maps);

    List<Setmeal> findByCondition(String queryString);
}
