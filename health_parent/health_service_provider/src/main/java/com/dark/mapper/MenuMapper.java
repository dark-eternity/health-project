package com.dark.mapper;

import com.dark.pojo.Menu;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MenuMapper {
    @Select("select * from t_menu")
    List<Menu> findAll();

    List<Menu> findParentMenu(String name);

    List<Menu> findChildMenu(Integer id);

    List<Menu> findChildMenuByIds(List<Integer> ids);

    List<Menu> findAllByUsername(String name);
}
