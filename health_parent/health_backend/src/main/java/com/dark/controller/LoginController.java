package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.constant.MessageConstant;
import com.dark.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/login")
public class LoginController {
    @Autowired
    private Result result;

    @RequestMapping(path = "/findUsername")
    public Result findUsername() {
        try {
            //正常获取用户名
            String name = SecurityContextHolder.getContext().getAuthentication().getName();
            //设置正确相应数据
            result.setFlag(true);
            result.setMessage(MessageConstant.GET_USERNAME_SUCCESS);
            result.setData(name);
        } catch (Exception ex) {
            //获取用户名异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage(MessageConstant.GET_USERNAME_FAIL);
            result.setData(null);
        }
        return result;
    }

    @RequestMapping(path = "/findError")
    public Result findError(HttpSession session) {
        try {
            //正常获取异常信息
            Exception error = (Exception) session.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
            //设置正确相应数据
            result.setFlag(true);
            result.setMessage("获取异常信息成功！");
            result.setData(error.getMessage());
        } catch (Exception ex) {
            //获取异常信息失败
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage("获取异常信息失败！");
            result.setData(null);
        }
        return result;
    }
}
