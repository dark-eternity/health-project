package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.mapper.SetmealMapper;
import com.dark.pojo.Setmeal;
import com.dark.service.SetmealService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private PageResult pageResult;

    @Override
    public void add(Integer[] checkgroupIds, Setmeal setmeal) {
        //调用mapper
        //添加套餐表数据，更新套餐实例对象id属性
        setmealMapper.add(setmeal);
        //调用方法往中间表中添加数据
        addMealAnnoGroup(setmeal.getId(), checkgroupIds);
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        //初始化分页参数
        Integer currentPage = 0;
        Integer pageSize = 0;
        String queryString = null;
        //获取分页参数
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
            pageSize = 3;
        }
        if (queryString != null) {
            if (queryString.length() <= 0) {
                queryString = null;
            } else {
                queryString = new StringBuilder().append("%").append(queryString).append("%").toString();
            }
            currentPage = 1;
        }
        //设置分页条件
        PageHelper.startPage(currentPage, pageSize);
        //调用mapper
        List<Setmeal> list = setmealMapper.findByCondition(queryString);
        //设置返回值
        if (list != null && list.size() > 0) {
            Page<Setmeal> page = (Page<Setmeal>) list;
            pageResult.setTotal(page.getTotal());
            pageResult.setRows(page.getResult());
            return pageResult;
        } else {
            return null;
        }
    }

    @Override
    public void deleteById(Integer id) {
        //调用mapper
        //删除中间表中套餐id与检查组间的联系
        setmealMapper.deleteMealAnnoGroup(id);
        //删除套餐表中id对应的数据
        setmealMapper.deleteById(id);
    }

    @Override
    public List<Integer> findMealAnnoGroup(Integer id) {
        //调用mapper
        if (id != null && id > 0) {
            return setmealMapper.findMealAnnoGroup(id);
        } else {
            return null;
        }
    }

    @Override
    public void update(Integer[] checkgroupIds, Setmeal setmeal) {
        //调用mapper
        //更改套餐表中指定id对应的数据
        setmealMapper.update(setmeal);
        //更改中间表中指定id对应的检查组联系
        updateMealAnnoGroup(setmeal.getId(), checkgroupIds);
    }

    @Override
    public Setmeal findById(Integer id) {
        //调用mapper
        Setmeal setmeal = setmealMapper.findById(id);
        return setmeal;
    }

    private void updateMealAnnoGroup(Integer id, Integer[] checkgroupIds) {
        //删除指定id对应的检查组联系
        setmealMapper.deleteMealAnnoGroup(id);
        //新增指定id对应的指定检查组联系
        addMealAnnoGroup(id, checkgroupIds);
    }

    private void addMealAnnoGroup(Integer id, Integer[] checkgroupIds) {
        //调用mapper
        //封装id与ids数组为list集合
        List<Map<String, Object>> maps = new ArrayList<>();
        if (checkgroupIds != null && checkgroupIds.length > 0) {
            //遍历ids数组
            for (Integer checkgroupId : checkgroupIds) {
                Map<String, Object> map = new HashMap<>();
                map.put("mealId", id);
                map.put("groupId", checkgroupId);
                maps.add(map);
            }
        }
        setmealMapper.addMealAnnoGroup(maps);
    }
}
