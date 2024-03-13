package com.of.fishapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.of.fishapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
} 