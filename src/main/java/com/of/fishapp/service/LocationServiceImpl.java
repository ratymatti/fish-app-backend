package com.of.fishapp.service;

import org.springframework.stereotype.Service;

import com.of.fishapp.entity.Location;
import com.of.fishapp.repository.LocationRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class LocationServiceImpl implements LocationService{
    
    private LocationRepository locationRepository;

    @Override
    public Location saveLocation(Location location) {
        if (location == null) throw new IllegalArgumentException("Location cannot be null");
        return locationRepository.save(location);
    }
}
