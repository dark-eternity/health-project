package com.dark.service;

import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void addGroup(Integer[] checkitemIds, CheckGroup checkGroup);

    void addGroupAnnoItem(Integer[] checkitemIds, Integer id);

    PageResult findByPage(QueryPageBean queryPageBean);

    List<Object> findById(Integer id);

    void update(Integer[] checkitemIds, CheckGroup checkGroup);

    List<CheckGroup> findAll();

    void deleteById(Integer id);
}
