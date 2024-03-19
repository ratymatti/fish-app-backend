package com.of.fishapp.repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.of.fishapp.entity.Fish;

public interface FishRepository extends JpaRepository<Fish, UUID>{
    List<Fish> findByUser_Id(UUID userId);        
}
