package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.constant.RedisMessageConstant;
import com.dark.entity.Result;
import com.dark.pojo.Order;
import com.dark.service.OrderService;
import com.dark.utils.DateUtils;
import com.dark.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(path = "/order")
public class OrderController {
    @Autowired
    private Result result;
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;

    @RequestMapping(path = "/submitOrder")
    public Result submitOrder(@RequestBody Map map) {
        try {
            //根据map中的手机号查询redis中相应的验证码
            String telephone = (String) map.get("telephone");
            String code_redis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            //对比redis中验证码与map中的验证码
            String validateCode = (String) map.get("validateCode");
            if (code_redis != null && validateCode != null && code_redis.equals(validateCode)) {
                //若相同，调用service
                //业务逻辑处理正常
                map.put("orderType", Order.ORDERTYPE_WEIXIN);
                Integer orderId = orderService.setmealOrder(map);
                if (orderId == 0) {
                    throw new IOException();
                }
                //设置正确响应数据
                result.setFlag(true);
                result.setMessage(MessageConstant.ORDERSETTING_SUCCESS);
                result.setData(orderId);
                //预约成功，发送短信
                SMSUtils.sendMessage(telephone, SMSUtils.ORDER_NOTICE, (String) map.get("orderDate"));
                //删除redis中的验证码
                jedisPool.getResource().del(telephone + RedisMessageConstant.SENDTYPE_ORDER);
            } else {
                //若不同，设置错误响应信息
                result.setFlag(false);
                result.setMessage("验证码错误或已失效！请填写正确的验证码或重新获取验证码..");
                result.setData(null);
            }
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.ORDERSETTING_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/findMsgById")
    public Result findMsgById(Integer id) {
        try {
            //业务逻辑处理正常
            Map map = orderService.findMsgById(id);
            //设置正确响应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.QUERY_ORDER_SUCCESS);
            result.setData(map);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.QUERY_ORDER_FAIL);
            result.setData(null);
        }
        return result;
    }
}
