package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.entity.Result;
import com.dark.pojo.OrderSetting;
import com.dark.service.OrderSettingService;
import com.dark.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/ordersetting")
public class OrderSettingController {
    @Autowired
    private Result result;
    @Reference
    private OrderSettingService orderSettingService;

    @RequestMapping(path = "/upload")
    public Result upload(MultipartFile excelFile) {
        try {
            //poi解析excel文件
            List<String[]> excel = POIUtils.readExcel(excelFile);
            //业务逻辑处理正常
            orderSettingService.addByExcel(excel);
            //设置正确响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            result.setData(null);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.IMPORT_ORDERSETTING_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/findByMonth")
    public Result findByMonth(String data) {
        try {
            //业务逻辑处理正常
            List<Map> list = orderSettingService.findByMonth(data);
            //设置正确响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_ORDER_SUCCESS);
            result.setData(list);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_ORDER_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/updateNumber")
    public Result updateNumber(@RequestBody OrderSetting orderSetting) {
        try {
            //业务逻辑处理正常
            orderSettingService.updateNumber(orderSetting);
            //设置正确响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.ORDERSETTING_SUCCESS);
            result.setData(null);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.ORDERSETTING_FAIL);
            result.setData(null);
        }
        return result;
    }
}
