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
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.service.WeatherObjectService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "http://localhost:3000")
@AllArgsConstructor
@RestController
@RequestMapping("/weather")
public class WeatherObjectController {
    
    WeatherObjectService weatherObjectService;

    @GetMapping("/{id}")
    public ResponseEntity<WeatherObject> findById(@PathVariable UUID id) {
        WeatherObject weatherObject = weatherObjectService.getWeatherObject(id);
        return new ResponseEntity<>(weatherObject, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WeatherObject>> getWeatherObjectsByUserId(@PathVariable UUID userId) {
        List<WeatherObject> weatherObjects = weatherObjectService.getWeatherObjects(userId);
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
        WeatherObject weatherObject = weatherObjectService.fetchAndSaveWeatherData(userId, location);
        return new ResponseEntity<>(weatherObject, HttpStatus.CREATED);
    }


}
