package com.of.fishapp.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.of.fishapp.client.WeatherApiClient;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.User;
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
        if (id == null)
            throw new IllegalArgumentException("ID cannot be null");
        Optional<WeatherObject> weatherObject = weatherObjectRepository.findById(id);
        return unwrapWeatherObject(weatherObject, id);
    }

    @Override
    public List<WeatherObject> getWeatherObjects(User user) {
        if (user == null)
            throw new IllegalArgumentException("ID cannot be null");
        return weatherObjectRepository.findByUser(user);
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
    public WeatherObject fetchAndSaveWeatherData(User user, Geolocation location) {

        if (location != null) {
            try {
                WeatherObject weatherObject = weatherApiClient.fetchWeatherData(location, "weather", user);
                if (weatherObject != null) {
                    weatherObject.setUser(user);
                    return weatherObjectRepository.save(weatherObject);
                }
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to fetch weather data");
            }
        } else {
            throw new IllegalArgumentException("Location cannot be null");
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteWeatherObject(UUID idToRemove) {
        if (idToRemove == null)
            throw new IllegalArgumentException("ID cannot be null");
        weatherObjectRepository.deleteById(idToRemove);
    }

    static WeatherObject unwrapWeatherObject(Optional<WeatherObject> entity, UUID id) {
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new EntityNotFoundException(id, WeatherObject.class);
        }
    }
}
