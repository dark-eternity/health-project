package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.entity.Result;
import com.dark.pojo.Menu;
import com.dark.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/menu")
public class MenuController {
    @Reference
    private MenuService menuService;
    @Autowired
    private Result result;

    @RequestMapping(path = "/findAll")
    public Result findAll() {
        try {
            //业务逻辑处理正常
            List<Menu> list = menuService.findAll();
            //设置正确响应数据
            result.setFlag(true);
            result.setMessage("菜单数据获取成功！");
            result.setData(list);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage("菜单数据获取失败！");
            result.setData(null);
        }
        return result;
    }
}
