package com.of.fishapp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.of.fishapp.entity.Fish;
import com.of.fishapp.repository.FishRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FishServiceImpl implements FishService {

    private FishRepository fishRepository;

    @Override
    public Fish saveFish(Fish fish) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Fish getFishById(UUID id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteFish(UUID id) {
        // TODO Auto-generated method stub

    }

    @Override
    public Fish updateFish(Fish fish) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Fish> getAllFishesByUserId(UUID userId) {
        // TODO Auto-generated method stub
        return null;
    }
}
