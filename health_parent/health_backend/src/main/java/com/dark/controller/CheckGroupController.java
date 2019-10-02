package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.entity.Result;
import com.dark.pojo.CheckGroup;
import com.dark.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/checkgroup")
public class CheckGroupController {
    @Autowired
    private Result result;
    @Autowired
    private PageResult pageResult;
    @Reference
    private CheckGroupService checkGroupService;

    @RequestMapping(path = "/addGroup")
    public Result addGroup(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup) {
        try {
            //业务逻辑处理正常
            checkGroupService.addGroup(checkitemIds, checkGroup);
            //设置信息
            result.setFlag(true);
            result.setMessage(MessageConstant.ADD_CHECKGROUP_SUCCESS);
            result.setData(null);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置信息
            result.setFlag(false);
            result.setMessage(MessageConstant.ADD_CHECKGROUP_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            //业务逻辑处理正常
            pageResult = checkGroupService.findByPage(queryPageBean);
            //设置响应数据
            return pageResult;
        } catch (Exception ex) {
            //业务逻辑处理异常
            return null;
        }
    }

    @RequestMapping(path = "/findById")
    public Result findById(Integer id) {
        try {
            //业务逻辑处理正常
            List<Object> list = checkGroupService.findById(id);
            //设置响应信息与数据
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(list);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_CHECKGROUP_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/update")
    public Result update(Integer[] checkitemIds, @RequestBody CheckGroup checkGroup) {
        try {
            //业务逻辑处理正常
            checkGroupService.update(checkitemIds, checkGroup);
            //设置响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.EDIT_CHECKGROUP_SUCCESS);
            result.setData(null);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.EDIT_CHECKGROUP_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/findAll")
    public Result findAll() {
        try {
            //业务逻辑处理正常
            List<CheckGroup> list = checkGroupService.findAll();
            //设置响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_CHECKGROUP_SUCCESS);
            result.setData(list);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_CHECKGROUP_FAIL);
            result.setData(null);
        }
        return result;
    }
}
