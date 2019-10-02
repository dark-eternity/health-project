package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.mapper.CheckItemMapper;
import com.dark.pojo.CheckItem;
import com.dark.service.CheckItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemMapper checkItemMapper;
    @Autowired
    private PageResult pageResult;

    @Override
    public void addCheck(CheckItem checkItem) {
        checkItemMapper.addCheck(checkItem);
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        //获取请求参数
        int currentPage = 0;
        int pageSize = 0;
        String queryString = null;
        if (queryPageBean != null) {
            currentPage = queryPageBean.getCurrentPage();
            pageSize = queryPageBean.getPageSize();
            queryString = queryPageBean.getQueryString();
        }
        //分页参数合理化
        if (currentPage <= 0) {
            currentPage = 1;
        }
        if (pageSize <= 0) {
            pageSize = 5;
        }
        //查询条件修饰
        if (queryString != null && queryString.length() > 0) {
            queryString = new StringBuilder().append("%").append(queryString).append("%").toString();
        }
        //设置分页条件
        PageHelper.startPage(currentPage, pageSize);
        //调用mapper
        List<CheckItem> byCondition = checkItemMapper.findByCondition(queryString);
        if (byCondition != null && byCondition.size() > 0) {
            Page<CheckItem> pageResults = (Page<CheckItem>) byCondition;
            pageResult.setTotal(pageResults.getTotal());
            pageResult.setRows(pageResults.getResult());
            return pageResult;
        } else {
            return null;
        }
    }

    @Override
    public CheckItem findById(Integer id) {
        //调用mapper
        CheckItem checkItem = checkItemMapper.findById(id);
        return checkItem;
    }

    @Override
    public void update(CheckItem checkItem) {
        //调用mapper
        checkItemMapper.update(checkItem);
    }

    @Override
    public void deleteById(Integer id) {
        //调用mapper
        checkItemMapper.deleteById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        //调用mapper
        List<CheckItem> list = checkItemMapper.findAll();
        return list;
    }
}
