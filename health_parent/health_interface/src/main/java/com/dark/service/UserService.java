package com.dark.service;

import com.dark.pojo.User;

public interface UserService {
    User findByUsername(String username);
}
