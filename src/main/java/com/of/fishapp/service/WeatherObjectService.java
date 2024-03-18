package com.of.fishapp.service;

import java.util.List;
import java.util.UUID;

import com.of.fishapp.entity.WeatherObject;

public interface WeatherObjectService {
    WeatherObject getWeatherObject(UUID id);
    List<WeatherObject> getWeatherObjects(UUID userId);
    WeatherObject saveWeatherObject(WeatherObject weatherObject);
}
