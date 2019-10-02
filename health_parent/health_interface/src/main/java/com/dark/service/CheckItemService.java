package com.dark.service;

import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {
    void addCheck(CheckItem checkItem);

    PageResult findByPage(QueryPageBean queryPageBean);

    CheckItem findById(Integer id);

    void update(CheckItem checkItem);

    void deleteById(Integer id);

    List<CheckItem> findAll();
}
