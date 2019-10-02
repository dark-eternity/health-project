package com.dark.mapper;

import com.dark.pojo.CheckGroup;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

public interface CheckGroupMapper {
    void addGroup(CheckGroup checkGroup);

    void addGroupAnnoItem(List<Map<String, Object>> maps);

    List<CheckGroup> findByCondition(String queryString);

    @Select("select * from t_checkgroup where id = #{value}")
    CheckGroup findById(Integer id);

    @Select("SELECT checkitem_id FROM t_checkgroup_checkitem WHERE checkgroup_id = #{value}")
    List<Integer> findItemIds(Integer id);

    void update(CheckGroup checkGroup);

    @Delete("delete from t_checkgroup_checkitem where checkgroup_id = #{value}")
    void deleteAnnoItem(Integer id);

    @Select("select * from t_checkgroup")
    List<CheckGroup> findAll();
}
