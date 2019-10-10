package com.dark.mapper;

import com.dark.pojo.Member;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

public interface MemberMapper {
    @Select("select * from t_member where phoneNumber = #{value}")
    Member findByTelephone(String telephone);

    Integer add(Map map);

    void addByMember(Member member);
}
