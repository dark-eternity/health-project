package com.dark.mapper;

import com.dark.pojo.Permission;

import java.util.Set;

public interface PermissionMapper {
    Set<Permission> findByRoleId(Integer id);
}
