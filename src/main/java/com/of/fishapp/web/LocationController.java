package com.of.fishapp.web;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.of.fishapp.entity.Location;
import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.repository.LocationRepository;
import com.of.fishapp.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;


@AllArgsConstructor
@RestController
@RequestMapping("/locations")
public class LocationController {

    private LocationRepository locationRepository;
    private UserRepository userRepository;

    @PostMapping("/create/{userId}")
    public ResponseEntity<Location> createLocation(@PathVariable UUID userId, @Valid @RequestBody Location location) {
        if (location == null) throw new IllegalArgumentException("Location cannot be null");

        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException(userId, User.class));
        location.setUser(user);

        locationRepository.save(location);
        return new ResponseEntity<>(location, HttpStatus.CREATED); 
    }

    // Add other methods as needed, such as update and delete
}