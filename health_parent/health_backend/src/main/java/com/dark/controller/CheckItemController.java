package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.entity.Result;
import com.dark.pojo.CheckItem;
import com.dark.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/checkitem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;
    @Autowired
    private Result result;
    @Autowired
    private PageResult pageResult;

    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    @RequestMapping(path = "/addCheck")
    public Result addCheck(@RequestBody CheckItem checkItem) {
        try {
            //业务逻辑正常处理
            checkItemService.addCheck(checkItem);
            //设置正常信息数据
            result.setFlag(true);
            result.setMessage(MessageConstant.ADD_CHECKGROUP_SUCCESS);
        } catch (Exception ex) {
            //业务处理异常
            //设置错误信息数据
            result.setFlag(false);
            result.setMessage(MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        return result;
    }

    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping(path = "/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            //业务逻辑处理正常
            PageResult byPage = checkItemService.findByPage(queryPageBean);
            return byPage;
        } catch (Exception ex) {
            //业务逻辑处理异常
            return null;
        }
    }

    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping(path = "findById")
    public Result findById(Integer id) {
        try {
            //业务逻辑处理正常
            CheckItem checkItem = checkItemService.findById(id);
            //设置响应信息与数据
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_SUCCESS);
            result.setData(checkItem);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return result;
    }

    @PreAuthorize("hasAuthority('CHECKITEM_EDIT')")
    @RequestMapping(path = "/update")
    public Result update(@RequestBody CheckItem checkItem) {
        try {
            //业务逻辑处理正常
            checkItemService.update(checkItem);
            //设置响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.EDIT_CHECKITEM_SUCCESS);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return result;
    }

    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    @RequestMapping(path = "/deleteById")
    public Result deleteById(Integer id) {
        try {
            //业务逻辑处理正常
            checkItemService.deleteById(id);
            //设置响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.DELETE_CHECKITEM_SUCCESS);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return result;
    }

    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    @RequestMapping(path = "/findAll")
    public Result findAll() {
        try {
            //业务逻辑处理正常
            List<CheckItem> list = checkItemService.findAll();
            //设置信息与数据
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_SUCCESS);
            result.setData(list);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return result;
    }
}
