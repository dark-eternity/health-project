package com.dark.service;

import com.dark.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    void addByExcel(List<String[]> excel);

    List<Map> findByMonth(String data);

    void updateNumber(OrderSetting orderSetting);
}
