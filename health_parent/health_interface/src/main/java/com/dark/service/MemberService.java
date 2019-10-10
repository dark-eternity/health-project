package com.dark.service;

import com.dark.pojo.Member;

public interface MemberService {
    Member findByTelephone(String telephone);

    void addByMember(Member member);
}
