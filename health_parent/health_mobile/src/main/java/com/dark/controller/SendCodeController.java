package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.constant.RedisMessageConstant;
import com.dark.entity.Result;
import com.dark.pojo.Order;
import com.dark.service.SendCodeService;
import com.dark.utils.SMSUtils;
import com.dark.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping(path = "/sendCode")
public class SendCodeController {
    @Autowired
    private Result result;
    @Reference
    private SendCodeService sendCodeService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(path = "/sendOrderCode")
    public Result sendOrderCode(String telephone) {
        try {
            //业务逻辑处理正常
            //生成随机验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //发送验证码
            SMSUtils.sendCode(telephone, SMSUtils.VALIDATE_CODE, String.valueOf(code));
            //将手机号+验证码存入redis
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_ORDER, 10 * 60, String.valueOf(code));
            //设置正确响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.SEND_VALIDATECODE_SUCCESS);
            result.setData(null);
            System.out.println(code);
        } catch (Exception ex) {
            //业务逻辑处理异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.SEND_VALIDATECODE_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/sendLoginCode")
    public Result sendLoginCode(String telephone) {
        try {
            //验证码正常发送
            //生成随机6位验证码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            //调用阿里大鱼发送短信
            SMSUtils.sendCode(telephone, SMSUtils.VALIDATE_CODE, String.valueOf(code));
            //使用电话号码组装key，将验证码存入redis
            jedisPool.getResource().setex(telephone + RedisMessageConstant.SENDTYPE_LOGIN, 10 * 60, String.valueOf(code));
            //设置成功响应信息
            result.setFlag(true);
            result.setMessage(MessageConstant.SEND_VALIDATECODE_SUCCESS);
            result.setData(null);
            System.out.println(code);
        } catch (Exception ex) {
            //验证码发送失败
            //提示失败响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.SEND_VALIDATECODE_FAIL);
            result.setData(null);
        }
        return result;
    }
}
