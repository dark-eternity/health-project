package com.dark.mapper;

import com.dark.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Select("select * from t_user where username = #{value}")
    User findByUsername(String username);
}
