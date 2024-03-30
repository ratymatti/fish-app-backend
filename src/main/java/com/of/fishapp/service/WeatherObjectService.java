package com.of.fishapp.service;

import java.util.List;
import java.util.UUID;

import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;

public interface WeatherObjectService {
    WeatherObject getWeatherObject(UUID id);
    List<WeatherObject> getWeatherObjects(User user);
    WeatherObject saveWeatherObject(WeatherObject weatherObject);
    WeatherObject fetchAndSaveWeatherData(User user, Geolocation location);
    void deleteWeatherObject(UUID idToRemove);
}
