package com.of.fishapp.service;

import java.util.UUID;

import com.of.fishapp.entity.User;

public interface UserService {
    User saveUser(User user);
    User getUser(UUID id);
    User getUserByGoogleId(String googleId);
} 