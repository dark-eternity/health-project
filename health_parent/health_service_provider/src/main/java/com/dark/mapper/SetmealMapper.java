package com.dark.mapper;

import com.dark.pojo.Setmeal;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface SetmealMapper {
    void add(Setmeal setmeal);

    void addMealAnnoGroup(List<Map<String, Object>> maps);

    List<Setmeal> findByCondition(String queryString);

    @Delete("delete from t_setmeal_checkgroup where setmeal_id = #{value}")
    void deleteMealAnnoGroup(Integer id);

    @Delete("delete from t_setmeal where id = #{value}")
    void deleteById(Integer id);

    @Select("select checkgroup_id from t_setmeal_checkgroup where setmeal_id = #{value}")
    List<Integer> findMealAnnoGroup(Integer id);

    void update(Setmeal setmeal);

    @Select("select * from t_setmeal where id = #{value}")
    Setmeal findById(Integer id);

    @Select("select * from t_setmeal")
    List<Setmeal> findAll();

    Setmeal findMsg(Integer id);
}
