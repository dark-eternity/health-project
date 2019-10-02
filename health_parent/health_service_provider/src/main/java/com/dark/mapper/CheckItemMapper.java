package com.dark.mapper;

import com.dark.pojo.CheckItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface CheckItemMapper {

    @Update("insert into t_checkitem values (null,#{code},#{name},#{sex},#{age},#{price},#{type},#{attention},#{remark})")
    void addCheck(CheckItem checkItem);

    List<CheckItem> findByCondition(String queryString);

    @Select("select * from t_checkitem where id = #{value}")
    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    @Delete("delete from t_checkitem where id = #{value}")
    void deleteById(Integer id);

    @Select("select * from t_checkitem")
    List<CheckItem> findAll();

    List<CheckItem> findByIds(List<Integer> list);
}
