package com.of.fishapp.service;

import com.of.fishapp.entity.User;

public interface UserService {
    User getUser(String username);
    User saveUser(User user);
    User getUser(Long id);
} 