package com.of.fishapp.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.of.fishapp.entity.Location;
import com.of.fishapp.entity.User;
import com.of.fishapp.service.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {
    
    UserService userService;

    @GetMapping("/{id}")
	public ResponseEntity<String> findById(@PathVariable UUID id) {
		return new ResponseEntity<>(userService.getUser(id).getUsername(), HttpStatus.OK);
	}

    @GetMapping("/{userId}/locations")
    public ResponseEntity<List<Location>> getLocationsByUserId(@PathVariable UUID userId) {
        return new ResponseEntity<>(userService.getUser(userId).getLocations(), HttpStatus.OK);
    }

    @PostMapping("/register")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        userService.saveUser(user);
		return new ResponseEntity<>(user, HttpStatus.CREATED); // REMOVE USER FROM THERE
	}
}
