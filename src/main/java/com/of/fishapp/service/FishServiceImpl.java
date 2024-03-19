package com.of.fishapp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.of.fishapp.entity.Fish;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.repository.FishRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FishServiceImpl implements FishService {

    private FishRepository fishRepository;

    @Override
    public Fish saveFish(Fish fish) {
        if (fish != null) {
            return fishRepository.save(fish);
        } else {
            throw new IllegalArgumentException("Fish cannot be null");
        }
    }

    @Override
    public Optional<Fish> getFishById(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        return fishRepository.findById(id);
    }

    @Override
    public void deleteFish(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        fishRepository.deleteById(id);
    }

    @Override
    public Fish updateFish(Fish fish) {
        if (fish == null) throw new IllegalArgumentException("Fish cannot be null");
        return fishRepository.save(fish);
    }

    @Override
    public List<Fish> getAllFishesByUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("ID cannot be null");
        return fishRepository.findByUser_Id(userId);
    }

    static Fish unwrapFish(Optional<Fish> entity, UUID id) {
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new EntityNotFoundException(id, Fish.class);
        }
    }
}
