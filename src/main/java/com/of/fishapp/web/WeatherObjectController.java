package com.of.fishapp.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.dto.IdToken;
import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.service.FirebaseAuthenticator;
import com.of.fishapp.service.UserService;
import com.of.fishapp.service.WeatherObjectService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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


    @PostMapping("/fetch/tracking")
    public ResponseEntity<WeatherObject> fetchAndSaveWeather(@Valid @RequestBody Geolocation location,
            @RequestHeader("Authorization") IdToken idToken) {

        try {
            User user = authenticator.validateUser(idToken);
            WeatherObject weatherObject = weatherObjectService.fetchAndSaveWeatherData(user, location);
            return new ResponseEntity<>(weatherObject, HttpStatus.CREATED);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/fetch/current")
    public ResponseEntity<WeatherObject> fetchCurrentWeather(@Valid @RequestBody Geolocation location,
            @RequestHeader("Authorization") IdToken idToken) {

        try {
            User user = authenticator.validateUser(idToken);
            WeatherObject weatherObject = weatherObjectService.fetchCurrentWeather(user, location);
            return new ResponseEntity<>(weatherObject, HttpStatus.OK);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete/{idToRemove}")
    public ResponseEntity<Void> deleteWeatherObject(@PathVariable UUID idToRemove, @RequestHeader("Authorization") IdToken idToken) {
        try {
            authenticator.validateUser(idToken);
            weatherObjectService.deleteWeatherObject(idToRemove);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/user/trackings")
    public ResponseEntity<List<WeatherObject>> getUserTrackings(@RequestHeader("Authorization") IdToken idToken) {
        try {
            User user = authenticator.validateUser(idToken);
            List<WeatherObject> userTrackings = weatherObjectService.getWeatherObjects(user);
            return new ResponseEntity<>(userTrackings, HttpStatus.OK);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/update/{weatherId}")
    public ResponseEntity<WeatherObject> updateWeatherObject(@PathVariable UUID weatherId,
            @RequestHeader("Authorization") IdToken idToken) {
        try {
            authenticator.validateUser(idToken);
            WeatherObject updatedWeatherObject = weatherObjectService.updateWeatherObject(weatherId);
            return new ResponseEntity<>(updatedWeatherObject, HttpStatus.OK);
        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
