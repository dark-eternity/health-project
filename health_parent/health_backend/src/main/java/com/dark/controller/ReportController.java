package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.entity.Result;
import com.dark.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping(path = "/report")
public class ReportController {
    @Autowired
    private Result result;
    @Reference
    private ReportService reportService;

    @RequestMapping(path = "/getMemberReport")
    public Result getMemberReport() {
        //获取至今过去一年的12个月中每月的会员数量统计
        //result{flag:bl,message:str,data:{"months":["2018.05","2018.06","2018.07"],"memberCount":[100,200,300]}}
        try {
            //获取会员数据成功
            Map<String, List<Object>> map = reportService.findMemberReport();
            //设置正确响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS);
            result.setData(map);
        } catch (Exception ex) {
            //获取会员数据失败
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/getSetmealReport")
    public Result getSetmealReport() {
        //数据模型：data:Map<String,List<Object>>,,setmealCount:List<Map<String,Object>>
        // result{flag:?,message:?,
        //      data:{"setmealNames":["xx","xx","xx"],
        //          "setmealCount":[{"value":xx,"name":"xxx"},{"value":xx,"name":"xxx"},{"value":xx,"name":"xxx"}]}}
        try {
            //获取套餐预约统计数据成功
            Map<String, List> map = reportService.findSetmealReport();
            //设置正确正确响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS);
            result.setData(map);
        } catch (Exception ex) {
            //获取套餐预约统计数据出现异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/getBusinessReportData")
    public Result getBusinessReportData() {
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
        try {
            //获取运营统计数据成功
            Map<String, Object> map = reportService.findBusinessReport();
            //设置正确响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.GET_BUSINESS_REPORT_SUCCESS);
            result.setData(map);
        } catch (Exception ex) {
            //获取运营统计数据出现异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.GET_BUSINESS_REPORT_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/getMemberReportBySex")
    public Result getMemberReportBySex() {
        //result{flag:x,message:x,data:[{name:'男性',value:5},{name:'女性',value:7}]}
        try {
            //获取会员数据成功
            List<Map<String, Object>> list = reportService.findMemberReportBySex();
            //设置成功响应数据
            result.setFlag(true);
            result.setMessage("会员数据获取成功！");
            result.setData(list);
        } catch (Exception ex) {
            //获取会员数据失败
            //设置失败响应信息
            result.setFlag(false);
            result.setMessage("会员数据获取失败！");
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/getMemberReportByAge")
    public Result getMemberReportByAge() {
        //result{flag:x,message:x,data:[{name:'0-18',value:5},{name:'18-30',value:7}...]}
        try {
            //根据年龄段查询会员数据成功
            Map<String, List> map = reportService.findMemberReportByAge();
            //设置成功响应数据
            result.setFlag(true);
            result.setMessage("会员数据查询成功！");
            result.setData(map);
        } catch (Exception ex) {
            //根据年龄段查询会员数据失败
            //设置失败响应数据
            result.setFlag(false);
            result.setMessage("会员数据查询失败！");
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/getMemberReportByDate")
    public Result getMemberReportByDate(@RequestBody Map<String, Date> map) {
        //获取beginTime到endTime中每月的会员数量统计
        //data:{"months":["2018.05","2018.06"...],"memberCount":[100,200...]}
        try {
            //会员数据查询成功
            Map<String, List> data = reportService.findMemberReportByDate(map);
            //设置成功响应数据
            result.setFlag(true);
            result.setMessage("会员数据查询成功！");
            result.setData(data);
        } catch (Exception ex) {
            //会员数据查询失败
            //设置失败响应信息
            result.setFlag(false);
            result.setMessage("会员数据查询失败！");
            result.setData(null);
        }
        return result;
    }
}
