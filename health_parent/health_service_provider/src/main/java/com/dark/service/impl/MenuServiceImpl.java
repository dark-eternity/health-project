package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.mapper.MenuMapper;
import com.dark.pojo.Menu;
import com.dark.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<Menu> findAll() {
        //调用mapper
        List<Menu> list = menuMapper.findAll();
        //声明子菜单集合
        List<Menu> subMenu = new ArrayList<>();
        List<Menu> subMenu_2 = new ArrayList<>();
        List<Menu> subMenu_3 = new ArrayList<>();
        List<Menu> subMenu_4 = new ArrayList<>();
        List<Menu> subMenu_5 = new ArrayList<>();
        //遍历菜单集合list
        for (Menu menu : list) {
            //判断当前菜单是否有父菜单
            Integer parentMenuId = menu.getParentMenuId();
            if (parentMenuId != null) {
                //有父菜单，添加到子菜单集合
                subMenu.add(menu);
                //添加到相应父菜单的子菜单列表
                switch (parentMenuId) {
                    case 2:
                        subMenu_2.add(menu);
                        break;
                    case 3:
                        subMenu_3.add(menu);
                        break;
                    case 4:
                        subMenu_4.add(menu);
                        break;
                    case 5:
                        subMenu_5.add(menu);
                        break;
                }
            }
        }
        //主菜单中移除子菜单
        list.removeAll(subMenu);
        //遍历新菜单集合list
        for (Menu menu : list) {
            //添加到相应父菜单的子菜单列表
            String path = menu.getPath();
            switch (Integer.parseInt(path)) {
                case 1:
                    break;
                case 2:
                    menu.setChildren(subMenu_2);
                    break;
                case 3:
                    menu.setChildren(subMenu_3);
                    break;
                case 4:
                    menu.setChildren(subMenu_4);
                    break;
                case 5:
                    menu.setChildren(subMenu_5);
                    break;
            }
        }
        return list;
    }
}
