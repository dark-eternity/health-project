package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.entity.PageResult;
import com.dark.entity.QueryPageBean;
import com.dark.entity.Result;
import com.dark.pojo.Permission;
import com.dark.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/permission")
public class PermissionController {
    @Autowired
    private Result result;
    @Autowired
    private PageResult pageResult;
    @Reference
    private PermissionService permissionService;

    //权限-增
    @RequestMapping(path = "/add")
    public Result add(@RequestBody Permission permission) {
        try {
            //权限数据添加成功
            permissionService.add(permission);
            //设置成功响应信息
            result.setFlag(true);
            result.setMessage("权限数据添加成功！");
            result.setData(null);
        } catch (Exception ex) {
            //权限数据添加成功
            //设置失败响应信息
            result.setFlag(false);
            result.setMessage("权限数据添加失败！");
            result.setData(null);
        }
        return result;
    }

    //权限-删
    @RequestMapping(path = "/deleteById")
    public Result deleteById(Integer id) {
        try {
            //删除权限数据成功
            permissionService.deleteById(id);
            //设置成功响应信息
            result.setFlag(true);
            result.setMessage("权限数据删除成功！");
            result.setData(null);
        } catch (Exception ex) {
            //删除权限数据失败
            //设置失败响应信息
            result.setFlag(false);
            result.setMessage("权限数据删除失败！");
            result.setData(null);
        }
        return result;
    }

    //权限-改
    @RequestMapping(path = "/update")
    public Result update(@RequestBody Permission permission) {
        try {
            //权限数据更改成功
            permissionService.update(permission);
            //设置成功响应信息
            result.setFlag(true);
            result.setMessage("权限数据更改成功！");
            result.setData(null);
        } catch (Exception ex) {
            //权限数据更改失败
            //设置失败响应信息
            result.setFlag(false);
            result.setMessage("权限数据更改失败！");
            result.setData(null);
        }
        return result;
    }

    //权限-查
    @RequestMapping(path = "/findByPage")
    public PageResult findByPage(@RequestBody QueryPageBean queryPageBean) {
        try {
            //查询所有权限成功
            pageResult = permissionService.findByPage(queryPageBean);
            //设置正确响应数据
            return pageResult;
        } catch (Exception ex) {
            //查询所有权限出现异常
            //设置错误响应信息
            return null;
        }
    }

    @RequestMapping(path = "/findById")
    public Result findById(Integer id) {
        try {
            //根据id查询权限数据成功
            Permission permission = permissionService.findById(id);
            //设置成功响应数据
            result.setFlag(true);
            result.setMessage("权限数据查询成功！");
            result.setData(permission);
        } catch (Exception ex) {
            //根据id查询权限数据失败
            //设置成功响应信息
            result.setFlag(false);
            result.setMessage("权限数据查询失败！");
            result.setData(null);
        }
        return result;
    }
}
