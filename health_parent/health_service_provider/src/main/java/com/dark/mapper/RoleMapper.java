package com.dark.mapper;

import com.dark.pojo.Role;

import java.util.Set;

public interface RoleMapper {
    Set<Role> findByUserId(Integer id);
}
