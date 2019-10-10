package com.dark.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.dark.constant.MessageConstant;
import com.dark.constant.RedisMessageConstant;
import com.dark.entity.Result;
import com.dark.pojo.Member;
import com.dark.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(path = "/member")
public class MemberController {
    @Autowired
    private Result result;
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;

    @RequestMapping(path = "/login")
    public Result login(@RequestBody Map loginInfo, HttpServletResponse response) {
        try {
            //正常登陆
            // 1、校验用户输入的短信验证码是否正确，如果验证码错误则登录失败
            //获取手机号与验证码
            String telephone = (String) loginInfo.get("telephone");
            String validateCode = (String) loginInfo.get("validateCode");
            //使用telephone组装key，从redis中查找验证码
            String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
            //对比验证码
            if (code != null && validateCode != null && code.equals(validateCode)) {
                // 2、如果验证码正确，则判断当前用户是否为会员，如果不是会员则自动完成会员注册
                Member member = memberService.findByTelephone(telephone);
                if (member == null) {
                    //不是会员
                    //注册成为会员
                    member = new Member();
                    member.setPhoneNumber(telephone);
                    member.setRegTime(new Date());
                    memberService.addByMember(member);
                }
                //登陆成功，设置正确响应信息
                result.setFlag(true);
                result.setMessage(MessageConstant.LOGIN_SUCCESS);
                result.setData(null);
                // 3、向客户端写入Cookie，内容为用户手机号
                Cookie cookie = new Cookie("member_telephone", telephone);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 15);
                response.addCookie(cookie);
                // 4、将会员信息保存到Redis，使用手机号作为key，保存时长为30分钟
                String member_redis = JSON.toJSONString(member);
                jedisPool.getResource().setex(telephone, 15 * 60 * 60, member_redis);
            } else {
                //验证码错误则登录失败
                //设置错误响应信息
                result.setFlag(false);
                result.setMessage(MessageConstant.VALIDATECODE_ERROR + "或已失效");
                result.setData(null);
            }
        } catch (Exception ex) {
            //登陆出现异常
            //设置错误响应信息
            result.setFlag(false);
            result.setMessage("登陆失败！");
            result.setData(null);
        }
        return result;
    }
}
