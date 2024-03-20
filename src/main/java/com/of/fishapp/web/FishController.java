package com.of.fishapp.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.of.fishapp.entity.Fish;
import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.FishService;
import com.of.fishapp.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
@RequestMapping("/fish")
public class FishController {
    
    FishService fishService;
    UserService userService;

    @GetMapping("/{fishId}")
    public ResponseEntity<Fish> findById(@PathVariable UUID fishId) {
        if (fishId == null) throw new IllegalArgumentException("ID cannot be null");
        Fish fish = fishService.getFishById(fishId);
        if (fish != null) {
            return new ResponseEntity<>(fish, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } 
    }

    @PostMapping("/save/{userId}")
    public ResponseEntity<Fish> saveFish(@Valid @RequestBody Fish fish, @PathVariable UUID userId) {
        User user = userService.getUser(userId);
        if (user  == null) throw new EntityNotFoundException(userId, User.class);
        fish.setUser(user);
        Fish savedFish = fishService.saveFish(fish);
        return new ResponseEntity<>(savedFish, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{fishId}")
    public ResponseEntity<Void> deleteFish(@PathVariable UUID fishId) {
        fishService.deleteFish(fishId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update/{fishId}")
    public ResponseEntity<Fish> updateFish(@PathVariable UUID fishId, @Valid @RequestBody Fish fish) {
        Fish updatedFish = fishService.updateFish(fish);
        return new ResponseEntity<>(updatedFish, HttpStatus.OK);
    }
    
}
