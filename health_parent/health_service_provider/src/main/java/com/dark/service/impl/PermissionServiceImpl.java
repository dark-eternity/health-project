package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.mapper.PermissionMapper;
import com.dark.pojo.Permission;
import com.dark.service.PermissionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = PermissionService.class)
@Transactional
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<Permission> findAll() {
        //调用mapper
        List<Permission> list = permissionMapper.findAll();
        return list;
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        //初始化查询参数
        Integer currentPage = 0;//当前页码
        Integer pageSize = 0;//每页记录数
        String queryString = "";//查询条件
        //获取查询参数
        if (queryPageBean != null) {
            currentPage = queryPageBean.getCurrentPage();
            pageSize = queryPageBean.getPageSize();
            queryString = queryPageBean.getQueryString();
        }
        //查询参数合理化
        if (currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize <= 0) {
            currentPage = 5;
        }
        if (queryString != null) {
            if (queryString.length() <= 0) {
                queryString = null;
            } else {
                queryString = new StringBuilder().append("%").append(queryString).append("%").toString();
            }
        }
        //设置pageHelper分页参数
        PageHelper.startPage(currentPage, pageSize);
        //调用mapper
        Page<Permission> page = (Page<Permission>) permissionMapper.findByCondition(queryString);
        //设置返回值
        PageResult result = new PageResult();
        result.setTotal(page.getTotal());
        result.setRows(page.getResult());
        return result;
    }

    @Override
    public void add(Permission permission) {
        //调用mapper
        if (permission != null) {
            permissionMapper.add(permission);
        }
    }

    @Override
    public Permission findById(Integer id) {
        //调用mapper
        Permission permission = permissionMapper.findById(id);
        return permission;
    }

    @Override
    public void update(Permission permission) {
        //调用mapper
        if (permission != null) {
            permissionMapper.update(permission);
        }
    }

    @Override
    public void deleteById(Integer id) {
        //调用mapper
        if (id > 0) {
            permissionMapper.deleteById(id);
        }
    }
}
