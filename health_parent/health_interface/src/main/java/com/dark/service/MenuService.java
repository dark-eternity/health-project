package com.dark.service;

import com.dark.pojo.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> findAll();

    List<Menu> findAllByUsername(String name);
}
