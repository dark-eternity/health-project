package com.dark.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dark.mapper.PermissionMapper;
import com.dark.mapper.RoleMapper;
import com.dark.mapper.UserMapper;
import com.dark.pojo.Permission;
import com.dark.pojo.Role;
import com.dark.pojo.User;
import com.dark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public User findByUsername(String username) {
        //调用mapper
        //根据用户名查询用户数据
        User user = userMapper.findByUsername(username);
        //根据用户id查询角色列表
        if (user != null) {
            Set<Role> roles = roleMapper.findByUserId(user.getId());
            //根据角色id查询权限列表
            if (roles != null && roles.size() > 0) {
                for (Role role : roles) {
                    Set<Permission> permissions = permissionMapper.findByRoleId(role.getId());
                    role.setPermissions(permissions);
                }
            }
            user.setRoles(roles);
        }
        return user;
    }
}
