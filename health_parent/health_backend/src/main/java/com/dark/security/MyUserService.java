package com.dark.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dark.pojo.Permission;
import com.dark.pojo.Role;
import com.dark.pojo.User;
import com.dark.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class MyUserService implements UserDetailsService {
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户数据
        User user = userService.findByUsername(username);
        if (user == null) {
            //用户信息无记录，登陆失败
            return null;
        }
        //初始化角色/权限列表
        List<GrantedAuthority> list = new ArrayList<>();
        //遍历用户的角色属性
        Set<Role> roles = user.getRoles();
        if (roles != null && roles.size() > 0) {
            for (Role role : roles) {
                //列表添加角色
                list.add(new SimpleGrantedAuthority(role.getKeyword()));
                //遍历角色的权限属性
                Set<Permission> permissions = role.getPermissions();
                for (Permission permission : permissions) {
                    //列表添加权限
                    list.add(new SimpleGrantedAuthority(permission.getKeyword()));
                }
            }
        }
        //设置返回值-用户名/密码/权限列表
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), list);
    }
}
