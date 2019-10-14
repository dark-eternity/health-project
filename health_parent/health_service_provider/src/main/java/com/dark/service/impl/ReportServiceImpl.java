package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.mapper.ReportMapper;
import com.dark.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportMapper reportMapper;

    @Override
    public Map<String, List<Object>> findMemberReport() throws Exception {
        //调用mapper
        Map<String, List<Object>> map = new HashMap<>();
        //months
        List<Object> months = new ArrayList<>();
        //orders
        List<Date> orders = new ArrayList<>();
        //counts
        Map<String, Object> counts = new HashMap<>();
        //memberCount
        List<Object> memberCount = new ArrayList<>();
        //前年今月-->今年今月
        for (int i = 12; i >= 0; i--) {
            //当前时间
            Calendar calendar = Calendar.getInstance();
            //i月之前
            calendar.add(Calendar.MONTH, -i);
            Date time = calendar.getTime();
            String format = new SimpleDateFormat("yyyy-MM").format(time);
            //添加months
            months.add(format);
            //根据月份查询会员数
            //    Integer count = reportMapper.findMemberCountByMonth(new SimpleDateFormat("yyyy-MM-dd").parse(format + "-31"));
            //添加memberCount
            //    memberCount.add(count);
            //添加orders
            orders.add(new SimpleDateFormat("yyyy-MM-dd").parse(format + "-31"));
        }
        //根据月份集合查询会员数集合
        counts = reportMapper.findMemberCountByOrders(orders);
        //添加memberCount
        for (int i = 0; i <= 12; i++) {
            memberCount.add(counts.get("count" + i));
        }
        //封装结果
        map.put("months", months);
        map.put("memberCount", memberCount);
        return map;
    }

    @Override
    public Map<String, List> findSetmealReport() {
        //数据模型：data:Map<String,List<Object>>,,setmealCount:List<Map<String,Object>>
        // result{flag:?,message:?,
        //      data:{"setmealNames":["xx","xx","xx"],
        //          "setmealCount":[{"value":xx,"name":"xxx"},{"value":xx,"name":"xxx"},{"value":xx,"name":"xxx"}]}}
        //调用mapper
        Map<String, List> map = new HashMap<>();
        //setmealNames
        List<String> setmealNames = new ArrayList<>();
        //setmealCount
        List<Map<String, Object>> setmealCount = new ArrayList<>();
        //根据套餐名分组查询套餐预约数
        //设置setmealCount
        setmealCount = reportMapper.findSetmealOrder();
        //遍历setmealCount
        for (Map<String, Object> objectMap : setmealCount) {
            //获取map中name键所对应的值，设置setmealNames
            setmealNames.add((String) objectMap.get("name"));
        }
        //封装返回值
        map.put("setmealNames", setmealNames);
        map.put("setmealCount", setmealCount);
        return map;
    }

    @Override
    public Map<String, Object> findBusinessReport() throws Exception {
        //data : reportData:    Map<String,Object>
        //          reportDate: null,           今日日期
        //          todayNewMember: 0,          今日新增会员数
        //          totalMember: 0,             总会员数
        //          thisWeekNewMember: 0,       本周新增会员数
        //          thisMonthNewMember: 0,      本月新增会员数
        //          todayOrderNumber: 0,        今日预约数
        //          todayVisitsNumber: 0,       今日到诊数
        //          thisWeekOrderNumber: 0,     本周预约数
        //          thisWeekVisitsNumber: 0,    本周到诊数
        //          thisMonthOrderNumber: 0,    本月预约数
        //          thisMonthVisitsNumber: 0,   本月到诊数
        //          hotSetmeal: [  热门套餐  -  套餐名称  -  预约数量  -  占比
        //                 {name: '阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐', setmeal_count: 200, proportion: 0.222},
        //                 {name: '阳光爸妈升级肿瘤12项筛查体检套餐', setmeal_count: 200, proportion: 0.222}
        //             ]
        Map<String, Object> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        //查询条件设置
        Map<String, Date> orders = new HashMap<>();
        //本日
        orders.put("today", new SimpleDateFormat("yyyy-MM-dd").parse(today));
        //本周一
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (week == 0) {
            week = 7;
        }
        calendar.add(Calendar.DATE, -(week - 1));
        String format = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
        orders.put("monday", new SimpleDateFormat("yyyy-MM-dd").parse(format));
        //本月初一
        calendar = Calendar.getInstance();
        format = new SimpleDateFormat("yyyy-MM").format(calendar.getTime());
        orders.put("first", new SimpleDateFormat("yyyy-MM-dd").parse(format + "-01"));
        //调用mapper
        map = reportMapper.findBusinessReport(orders);
        //reportDate
        map.put("reportDate", today);
        //hotSetmeal
        List<Map<String, Object>> setmealOrder = reportMapper.findSetmealOrder();
        map.put("hotSetmeal", setmealOrder);
        //设置返回值
        return map;
    }
}
