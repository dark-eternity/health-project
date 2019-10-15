package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.mapper.ReportMapper;
import com.dark.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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
        Map<String, Integer> counts = new LinkedHashMap<>();
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

    @Override
    public List<Map<String, Object>> findMemberReportBySex() {
        //result{flag:x,message:x,data:[{name:'男性',value:5},{name:'女性',value:7}]}
        //调用mapper
        Map<String, Integer> map = reportMapper.findMemberCountBySex();
        //设置返回值
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> hashMap01 = new HashMap<>();
        hashMap01.put("name", "男性");
        hashMap01.put("value", map.get("男性"));
        list.add(hashMap01);
        Map<String, Object> hashMap02 = new HashMap<>();
        hashMap02.put("name", "女性");
        hashMap02.put("value", map.get("女性"));
        list.add(hashMap02);
        return list;
    }

    @Override
    public Map<String, List> findMemberReportByAge() {
        //result{flag:x,message:x,data:[{name:'0-18',value:5},{name:'18-30',value:7}...]}
        Map<String, List> hashMap = new HashMap<>();
        //memberCount
        List<Map<String, Object>> list = new ArrayList<>();
        //ages
        List<String> ages = new ArrayList<>();

        //封装查询条件
        Map<String, Date> orders = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        //today/0
        //0
        Date time00 = calendar.getTime();
        orders.put("time00", time00);
        //0-18/18
        //18
        calendar.add(Calendar.YEAR, -18);
        Date time18 = calendar.getTime();
        orders.put("time18", time18);
        //18-30/12
        //30
        calendar.add(Calendar.YEAR, -12);
        Date time30 = calendar.getTime();
        orders.put("time30", time30);
        //30-45/15
        //45
        calendar.add(Calendar.YEAR, -15);
        Date time45 = calendar.getTime();
        orders.put("time45", time45);
        //45-60/15
        //60
        calendar.add(Calendar.YEAR, -15);
        Date time60 = calendar.getTime();
        orders.put("time60", time60);
        //60-80/20
        //80
        calendar.add(Calendar.YEAR, -20);
        Date time80 = calendar.getTime();
        orders.put("time80", time80);
        //80-100/20
        //100
        calendar.add(Calendar.YEAR, -20);
        Date time100 = calendar.getTime();
        orders.put("time100", time100);

        //调用mapper
        Map<String, Integer> result = reportMapper.findMemberCountByAge(orders);

        //设置返回值
        //0-18
        Map<String, Object> map18 = new HashMap<>();
        map18.put("name", "年龄段：0-18");
        map18.put("value", result.get("0to18"));
        list.add(map18);
        //18-30
        Map<String, Object> map30 = new HashMap<>();
        map30.put("name", "年龄段：18-30");
        map30.put("value", result.get("18to30"));
        list.add(map30);
        //30-45
        Map<String, Object> map45 = new HashMap<>();
        map45.put("name", "年龄段：30-45");
        map45.put("value", result.get("30to45"));
        list.add(map45);
        //45-60
        Map<String, Object> map60 = new HashMap<>();
        map60.put("name", "年龄段：45-60");
        map60.put("value", result.get("45to60"));
        list.add(map60);
        //60-80
        Map<String, Object> map80 = new HashMap<>();
        map80.put("name", "年龄段：60-80");
        map80.put("value", result.get("60to80"));
        list.add(map80);
        //80-100
        Map<String, Object> map100 = new HashMap<>();
        map100.put("name", "年龄段：80-100");
        map100.put("value", result.get("80to100"));
        list.add(map100);

        //设置memberCount
        hashMap.put("memberCount", list);

        //遍历list集合
        for (Map<String, Object> map : list) {
            ages.add((String) map.get("name"));
        }
        //设置ages
        hashMap.put("ages", ages);

        return hashMap;
    }

    @Override
    public Map<String, List> findMemberReportByDate(Map<String, Date> map) throws Exception {
        //获取beginTime到endTime中每月的会员数量统计
        //data:{"months":["2018.05","2018.06"...],"memberCount":[100,200...]}
        Date beginTime = null;
        Date endTime = null;
        if (map == null) {
            return null;
        }
        SimpleDateFormat temp = new SimpleDateFormat("yyyy-MM");
        beginTime = temp.parse(temp.format(map.get("beginTime")));
        endTime = temp.parse(temp.format(map.get("endTime")));
        if (beginTime.getTime() >= endTime.getTime()) {
            throw new IOException();
        }

        Map<String, List> result = new HashMap<>();
        //months
        List<String> months = new ArrayList<>();
        //orders
        List<Date> orders = new ArrayList<>();
        //counts
        Map<String, Integer> counts = new LinkedHashMap<>();
        //memberCount
        List<Integer> memberCount = new ArrayList<>();

        //beginTime --> endTime之间的月份
        //beginTime
        Calendar calendar_begin = Calendar.getInstance();
        calendar_begin.setTime(beginTime);
        //endTime
        Calendar calendar_end = Calendar.getInstance();
        calendar_end.setTime(endTime);
        //初始化months
        months.add(temp.format(beginTime));
        //初始化orders
        orders.add(new SimpleDateFormat("yyyy-MM-dd").parse(temp.format(beginTime) + "-31"));
        //设置死循环
        while (true) {
            //若begin与end时间重合，终止循环
            if (calendar_begin.getTimeInMillis() == calendar_end.getTimeInMillis()) {
                break;
            }
            //月份+1
            calendar_begin.add(Calendar.MONTH, 1);
            //months添加数据
            months.add(temp.format(calendar_begin.getTime()));
            //orders添加条件
            orders.add(new SimpleDateFormat("yyyy-MM-dd").parse(temp.format(calendar_begin.getTime()) + "-31"));
        }
        //根据月份集合查询会员数集合
        counts = reportMapper.findMemberCountByOrders(orders);
        //添加memberCount
        Set<Map.Entry<String, Integer>> entrySet = counts.entrySet();
        for (Map.Entry<String, Integer> entry : entrySet) {
            memberCount.add(entry.getValue());
        }
        //封装结果
        result.put("months", months);
        result.put("memberCount", memberCount);

        return result;
    }
}
