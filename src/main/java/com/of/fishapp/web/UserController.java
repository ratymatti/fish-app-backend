package com.of.fishapp.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.of.fishapp.entity.Location;
import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.google.firebase.auth.FirebaseAuthException;
import com.of.fishapp.dto.IdToken;
import com.of.fishapp.dto.UserDetails;
import com.of.fishapp.entity.Fish;
import com.of.fishapp.service.UserService;

import com.of.fishapp.service.FirebaseAuthenticator;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;
    FirebaseAuthenticator authenticator;

    @GetMapping("/{userId}")
    public ResponseEntity<User> findById(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        if (user == null)
            throw new EntityNotFoundException(userId, User.class);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{userId}/locations")
    public ResponseEntity<List<Location>> getLocationsByUserId(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        if (user == null)
            throw new EntityNotFoundException(userId, User.class);

        return new ResponseEntity<>(user.getLocations(), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<HttpStatus> authenticateUser(@Valid @RequestBody IdToken idToken) {
        try {
            if (!authenticator.verifyIdToken(idToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User existingUser = userService.getUserByGoogleId(authenticator.getUidFromToken(idToken));
            if (existingUser != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            }

            createUser(idToken);
            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{userId}/fishes")
    public ResponseEntity<List<Fish>> getFishesByUserId(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        if (user == null)
            throw new EntityNotFoundException(userId, User.class);

        return new ResponseEntity<>(user.getFishes(), HttpStatus.OK);
    }

    private void createUser(IdToken idToken) throws FirebaseAuthException {
        UserDetails userDetails = authenticator.getUserDetails(idToken);
        User newUser = new User();
        newUser.setGoogleId(userDetails.getGoogleId());
        newUser.setName(userDetails.getName());
        newUser.setEmail(userDetails.getEmail());
        userService.saveUser(newUser);
    }
}
