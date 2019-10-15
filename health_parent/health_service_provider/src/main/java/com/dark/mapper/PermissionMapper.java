package com.dark.mapper;

import com.dark.pojo.Permission;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Set;

public interface PermissionMapper {
    Set<Permission> findByRoleId(Integer id);

    @Select("select id,name,keyword,description from t_permission")
    List<Permission> findAll();

    List<Permission> findByCondition(String queryString);

    void add(Permission permission);

    @Select("select id,name,keyword,description from t_permission where id = #{value}")
    Permission findById(Integer id);

    void update(Permission permission);

    @Delete("delete from t_permission where id = #{value}")
    void deleteById(Integer id);
}
