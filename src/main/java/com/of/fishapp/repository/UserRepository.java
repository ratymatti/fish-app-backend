package com.of.fishapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.of.fishapp.entity.User;


public interface UserRepository extends JpaRepository<User, UUID>{
    public User findByGoogleId(String googleId);
} 