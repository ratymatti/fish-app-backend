package com.of.fishapp.service;

import java.util.UUID;

import com.of.fishapp.entity.Fish;

public interface FishService {
    Fish saveFish(Fish fish);
    Fish getFishById(UUID id);
    void deleteFish(UUID id);
    Fish updateFish(Fish fish);
}
