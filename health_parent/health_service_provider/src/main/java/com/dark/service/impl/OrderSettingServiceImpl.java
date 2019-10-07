package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.mapper.OrderSettingMapper;
import com.dark.pojo.OrderSetting;
import com.dark.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingMapper orderSettingMapper;

    @Override
    public void addByExcel(List<String[]> excel) {
        //调用mapper
        if (excel != null && excel.size() > 0) {
            //初始化ordersetting集合
            List<OrderSetting> orderSettings = new ArrayList<>();
            //遍历excel集合
            for (String[] cells : excel) {
                //初始化ordersetting对象
                OrderSetting orderSetting = new OrderSetting();
                if (cells != null && cells.length > 0) {
                    orderSetting.setOrderDate(new Date(cells[0]));
                    orderSetting.setNumber(Integer.parseInt(cells[1]));
                    orderSettings.add(orderSetting);
                }
            }
            //遍历ordersetting集合
            for (OrderSetting orderSetting : orderSettings) {
                //判断当前套餐预约项是否已存在
                Date orderDate = orderSetting.getOrderDate();
                OrderSetting os = orderSettingMapper.findByDate(orderDate);
                if (os == null) {
                    //不存在，添加
                    orderSettingMapper.add(orderSetting);
                } else {
                    //存在，更改
                    orderSettingMapper.update(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> findByMonth(String data) {
        //月初
        String begin = data + "-1";
        //月末
        String end = data + "-31";
        //map封装
        Map<String, String> map = new HashMap<>();
        map.put("begin", begin);
        map.put("end", end);
        //调用mapper
        List<OrderSetting> list = orderSettingMapper.findByMonth(map);
        //遍历list集合
        List<Map> mapList = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (OrderSetting orderSetting : list) {
                Map<String, Object> objectHashMap = new HashMap<>();
                //date
                objectHashMap.put("date", orderSetting.getOrderDate().getDate());
                //number
                objectHashMap.put("number", orderSetting.getNumber());
                //reservations
                objectHashMap.put("reservations", orderSetting.getReservations());
                mapList.add(objectHashMap);
            }
        }
        return mapList;
    }

    @Override
    public void updateNumber(OrderSetting orderSetting) {
        //调用mapper
        //查找当前设置套餐日期是否存在
        OrderSetting byDate = orderSettingMapper.findByDate(orderSetting.getOrderDate());
        if (byDate != null) {
            //存在，更改
            orderSettingMapper.update(orderSetting);
        } else {
            //不存在，添加
            orderSettingMapper.add(orderSetting);
        }
    }
}
