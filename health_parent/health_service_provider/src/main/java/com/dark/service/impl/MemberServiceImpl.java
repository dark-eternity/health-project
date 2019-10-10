package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.mapper.MemberMapper;
import com.dark.pojo.Member;
import com.dark.service.MemberService;
import com.dark.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Member findByTelephone(String telephone) {
        //调用mapper
        Member byTelephone = memberMapper.findByTelephone(telephone);
        return byTelephone;
    }

    @Override
    public void addByMember(Member member) {
        //调用mapper
        //加密
        String password = member.getPassword();
        if (password != null && password.length() > 0) {
            String newPassword = MD5Utils.md5(password);
            member.setPassword(newPassword);
        }
        memberMapper.addByMember(member);
    }
}
