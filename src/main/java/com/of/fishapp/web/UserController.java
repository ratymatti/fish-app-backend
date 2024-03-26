package com.of.fishapp.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    UserService userService;
    FirebaseAuthenticator authenticator;

    @GetMapping("/id/{userId}")
    public ResponseEntity<User> findById(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        if (user == null)
            throw new EntityNotFoundException(userId, User.class);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/id/{userId}/locations")
    public ResponseEntity<List<Location>> getLocationsByUserId(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        if (user == null)
            throw new EntityNotFoundException(userId, User.class);

        return new ResponseEntity<>(user.getLocations(), HttpStatus.OK);
    }

    @GetMapping("/fishes")
    public ResponseEntity<List<Fish>> getFishesByUserId(@RequestHeader("Authorization") IdToken idToken) {
        try {
            idToken.setIdToken(idToken.getIdToken().substring(7)); // Remove "Bearer " from the token
            if (!authenticator.verifyIdToken(idToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String googleId = authenticator.getUidFromToken(idToken);
            User user = userService.getUserByGoogleId(googleId);

            if (user == null) {
                throw new EntityNotFoundException(User.class);
            }

            List<Fish> userFishes = user.getFishes();

            return new ResponseEntity<>(userFishes, HttpStatus.OK);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody IdToken idToken) {
        try {
            if (!authenticator.verifyIdToken(idToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }

            User existingUser = userService.getUserByGoogleId(authenticator.getUidFromToken(idToken));
            if (existingUser != null) {
                return new ResponseEntity<>(HttpStatus.OK);
            }

            User newUser = createUser(idToken);
            userService.saveUser(newUser);

            return new ResponseEntity<>(HttpStatus.CREATED);

        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    private User createUser(IdToken idToken) throws FirebaseAuthException {
        UserDetails userDetails = authenticator.getUserDetails(idToken);
        User user = new User();
        user.setGoogleId(userDetails.getGoogleId());
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        return user;
    }
}
