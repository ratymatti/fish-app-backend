package com.of.fishapp.service;

import java.util.List;
import java.util.UUID;

import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.Fish;
import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;

public interface WeatherObjectService {
    List<WeatherObject> getWeatherObjects(User user);
    WeatherObject fetchAndSaveWeatherData(User user, Geolocation location);
    void deleteWeatherObject(UUID idToRemove);
    WeatherObject fetchCurrentWeather(User user, Geolocation location);
    WeatherObject updateWeatherObject(UUID id);
    WeatherObject fetchAndSaveWeatherDataForFish(Fish fish);
}
