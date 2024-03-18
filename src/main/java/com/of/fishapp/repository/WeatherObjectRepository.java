package com.of.fishapp.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.of.fishapp.entity.WeatherObject;

@Repository
public interface WeatherObjectRepository extends JpaRepository<WeatherObject, UUID>{
    WeatherObject findWeatherById(UUID id);
    List<WeatherObject> findByUserId(UUID userId);
}
