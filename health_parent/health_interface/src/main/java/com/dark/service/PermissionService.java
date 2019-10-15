package com.dark.service;

import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.pojo.Permission;

import java.util.List;

public interface PermissionService {
    List<Permission> findAll();

    PageResult findByPage(QueryPageBean queryPageBean);

    void add(Permission permission);

    Permission findById(Integer id);

    void update(Permission permission);

    void deleteById(Integer id);
}
