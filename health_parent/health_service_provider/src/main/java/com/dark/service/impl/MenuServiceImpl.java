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
        List<Menu> subMenu_6 = new ArrayList<>();
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
                    case 6:
                        subMenu_6.add(menu);
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
                case 6:
                    menu.setChildren(subMenu_6);
                    break;
            }
        }
        return list;
    }

    @Override
    public List<Menu> findAllByUsername(String name) {
        //通过用户名从user表中查找uid,通过uid从user-role表中查找rid,通过rid从role-menu表中查找mid,
        // 通过mid从menu表中查找level=1的pmenu,通过pmenu的pid从menu表中查找parentMenuId=pid的cmenu,
        // 将cmenu存入pmenu的child列表中
        List<Menu> menuList = new ArrayList<>();
//        //根据用户名查询一级菜单列表
//        List<Menu> parent = menuMapper.findParentMenu(name);
//        List<Integer> ids = new ArrayList<>();
//        if (parent != null && parent.size() > 0) {
//            //遍历一级菜单
//            for (Menu parentMenu : parent) {
////                //根据父菜单id查询二级菜单列表
////                List<Menu> childMenu = menuMapper.findChildMenu(parentMenu.getId());
////                parentMenu.setChildren(childMenu);
////                //设置返回值菜单列表
////                menuList.add(parentMenu);
//                //获取父菜单id列表
//                ids.add(parentMenu.getId());
//            }
//            if (ids != null && ids.size() > 0) {
//                //根据父菜单id列表查询二级菜单列表
//                List<Menu> child = menuMapper.findChildMenuByIds(ids);
//                if (child != null && child.size() > 0) {
//                    //遍历一级菜单
//                    for (Menu parentMenu : parent) {
//                        List<Menu> childs = new ArrayList<>();
//                        //遍历二级菜单
//                        for (Menu childMenu : child) {
//                            Integer parentMenuId = childMenu.getParentMenuId();
//                            if (parentMenu.getId() == parentMenuId) {
//                                childs.add(childMenu);
//                            }
//                        }
//                        //一级菜单添加属于自己的二级菜单
//                        parentMenu.setChildren(childs);
//                        //设置返回值菜单列表
//                        menuList.add(parentMenu);
//                    }
//                }
//            }
//        }
        //调用mapper
        menuList = menuMapper.findAllByUsername(name);
        return menuList;
    }
}
