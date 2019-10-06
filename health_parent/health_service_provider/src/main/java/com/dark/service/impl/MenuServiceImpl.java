package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.mapper.MenuMapper;
import com.dark.pojo.Menu;
import com.dark.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> findAll() {
        //调用mapper
        List<Menu> list = menuMapper.findAll();
        return list;
    }
}
