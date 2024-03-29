package com.of.fishapp.web;

import static com.of.fishapp.util.IdTokenUtil.removeBearerPrefix;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.dto.IdToken;
import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.FirebaseAuthenticator;
import com.of.fishapp.service.UserService;
import com.of.fishapp.service.WeatherObjectService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherObjectController {

    WeatherObjectService weatherObjectService;
    UserService userService;
    FirebaseAuthenticator authenticator;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WeatherObject>> getWeatherObjectsByUserId(@PathVariable UUID userId) {
        User user = userService.getUser(userId);
        List<WeatherObject> weatherObjects = weatherObjectService.getWeatherObjects(user);
        return new ResponseEntity<>(weatherObjects, HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<WeatherObject> saveWeatherObject(@Valid @RequestBody WeatherObject weatherObject) {
        WeatherObject savedWeatherObject = weatherObjectService.saveWeatherObject(weatherObject);
        return new ResponseEntity<>(savedWeatherObject, HttpStatus.CREATED);
    }

    @PostMapping("/fetch/current")
    public ResponseEntity<WeatherObject> fetchAndSaveWeather(@Valid @RequestBody Geolocation location,
            @RequestHeader("Authorization") IdToken idToken) {

        try {
            removeBearerPrefix(idToken);
            if (!authenticator.verifyIdToken(idToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            
            String googleId = authenticator.getUidFromToken(idToken);
            User user = userService.getUserByGoogleId(googleId);

            if (user == null) {
                throw new EntityNotFoundException(User.class);
            }
            WeatherObject weatherObject = weatherObjectService.fetchAndSaveWeatherData(user, location);
            return new ResponseEntity<>(weatherObject, HttpStatus.CREATED);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
