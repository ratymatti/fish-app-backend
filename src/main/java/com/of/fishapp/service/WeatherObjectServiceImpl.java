package com.of.fishapp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.of.fishapp.client.WeatherApiClient;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.repository.WeatherObjectRepository;

import com.of.fishapp.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class WeatherObjectServiceImpl implements WeatherObjectService {

    private WeatherObjectRepository weatherObjectRepository;
    private WeatherApiClient weatherApiClient;

    @Override
    public WeatherObject getWeatherObject(UUID id) {
        if (id == null) throw new IllegalArgumentException("ID cannot be null");
        Optional<WeatherObject> weatherObject = weatherObjectRepository.findById(id);
        return unwrapWeatherObject(weatherObject, id);
    }

    @Override
    public List<WeatherObject> getWeatherObjects(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("ID cannot be null");
        return weatherObjectRepository.findByUserId(userId);
    }

    @Override
    public WeatherObject saveWeatherObject(WeatherObject weatherObject) {
        if (weatherObject != null) {
            return weatherObjectRepository.save(weatherObject);
        } else {
            throw new IllegalArgumentException("WeatherObject cannot be null");
        }    
    }

    @Override
    public WeatherObject fetchAndSaveWeatherData(UUID userId, Geolocation location) {
        WeatherObject weatherObject;
        if (location != null) {
            try {
               weatherObject = weatherApiClient.fetchWeatherData(location, "weather", userId);
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to fetch weather data");
            }
        } else {
            throw new IllegalArgumentException("Location cannot be null");
        }

        if (weatherObject != null) weatherObjectRepository.save(weatherObject);

        return weatherObject;
    }

    static WeatherObject unwrapWeatherObject(Optional<WeatherObject> entity, UUID id) {
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new EntityNotFoundException(id, WeatherObject.class);
        }    
    }
}
