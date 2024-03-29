package com.of.fishapp.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.service.UserService;
import com.of.fishapp.service.WeatherObjectService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@NoArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherObjectController {
    
    WeatherObjectService weatherObjectService;
    UserService userService;

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
    
    @PostMapping("/{userId}/{lat}/{lng}")
    public ResponseEntity<WeatherObject> getAndSaveWeather(@PathVariable UUID userId, @PathVariable double lat, @PathVariable double lng) {
        Geolocation location = new Geolocation(lat, lng);
        User user = userService.getUser(userId);
        WeatherObject weatherObject = weatherObjectService.fetchAndSaveWeatherData(user, location);
        return new ResponseEntity<>(weatherObject, HttpStatus.CREATED);
    }


}
