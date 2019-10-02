package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.mapper.CheckGroupMapper;
import com.dark.mapper.CheckItemMapper;
import com.dark.pojo.CheckGroup;
import com.dark.pojo.CheckItem;
import com.dark.service.CheckGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupMapper checkGroupMapper;
    @Autowired
    private CheckItemMapper checkItemMapper;
    @Autowired
    private PageResult pageResult;

    @Override
    public void addGroup(Integer[] checkitemIds, CheckGroup checkGroup) {
        //调用mapper
        //添加检查组数据，更新checkGroup
        checkGroupMapper.addGroup(checkGroup);
        //添加检查组-检查项中间表数据
        addGroupAnnoItem(checkitemIds, checkGroup.getId());
    }

    public void addGroupAnnoItem(Integer[] checkitemIds, Integer id) {
        //将组id与项id列表封装为list
        List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        if (id > 0 && checkitemIds != null && checkitemIds.length > 0) {
            //遍历项id列表
            for (Integer checkitemId : checkitemIds) {
                Map<String, Object> map = new HashMap<>();
                map.put("groupId", id);
                map.put("itemId", checkitemId);
                maps.add(map);
            }
            //调用mapper
            checkGroupMapper.addGroupAnnoItem(maps);
        }
    }

    @Override
    public PageResult findByPage(QueryPageBean queryPageBean) {
        //初始化分页参数
        Integer currentPage = 0;
        Integer pageSize = 0;
        String queryString = null;
        if (queryPageBean != null) {
            //获取分页参数
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
                currentPage = 1;
            }
        }
        //设置分页条件
        PageHelper.startPage(currentPage, pageSize);
        //调用mapper
        List<CheckGroup> list = checkGroupMapper.findByCondition(queryString);
        if (list == null || list.size() <= 0) {
            return null;
        } else {
            Page<CheckGroup> page = (Page<CheckGroup>) list;
            pageResult.setTotal(page.getTotal());
            pageResult.setRows(page.getResult());
            return pageResult;
        }
    }

    @Override
    public List<Object> findById(Integer id) {
        //调用mapper
        List<Object> objects = new ArrayList<>();
        //通过id从检查组表中查询检查组数据
        CheckGroup checkGroup = checkGroupMapper.findById(id);
        objects.add(checkGroup);
        //通过id从中间表中查询检查项id列表
        List<Integer> ids = checkGroupMapper.findItemIds(id);
        objects.add(ids);
        //从检查项表中查询检查项数据
        List<CheckItem> checkItems = checkItemMapper.findAll();
        objects.add(checkItems);
        return objects;
    }

    @Override
    public void update(Integer[] checkitemIds, CheckGroup checkGroup) {
        //调用mapper
        //更新检查组数据
        checkGroupMapper.update(checkGroup);
        //根据id删除中间表中特定检查组的数据
        checkGroupMapper.deleteAnnoItem(checkGroup.getId());
        //添加新中间表数据
        if (checkitemIds != null && checkitemIds.length > 0) {
            addGroupAnnoItem(checkitemIds, checkGroup.getId());
        }
    }

    @Override
    public List<CheckGroup> findAll() {
        //调用mapper
        List<CheckGroup> list = checkGroupMapper.findAll();
        return list;
    }
}
