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
    public List<WeatherObject> getWeatherObjects(User user) {
        if (user == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return weatherObjectRepository.findByUser(user);
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
    public WeatherObject fetchCurrentWeather(User user, Geolocation location) {
        if (location != null) {
            try {
                WeatherObject weatherObject = weatherApiClient.fetchWeatherData(location, "weather", user);
                if (weatherObject != null) {
                    weatherObject.setUser(user);
                    return weatherObject;
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
        if (idToRemove == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        weatherObjectRepository.deleteById(idToRemove);
    }

    @Override
    public WeatherObject updateWeatherObject(UUID weatherId) {
        if (weatherId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        Optional<WeatherObject> entity = weatherObjectRepository.findById(weatherId);
        WeatherObject existingWeatherObject = unwrapWeatherObject(entity, weatherId);

        WeatherObject updatedWeatherObject = fetchCurrentWeather(existingWeatherObject.getUser(),
                existingWeatherObject.getCoords());
        updateExistingWeatherObject(existingWeatherObject, updatedWeatherObject);

        return weatherObjectRepository.save(existingWeatherObject);
    }

    private void updateExistingWeatherObject(WeatherObject existingWeatherObject, WeatherObject updatedWeatherObject) {
        existingWeatherObject.setCurrentWeather(updatedWeatherObject.getCurrentWeather());
        existingWeatherObject.setInfo(updatedWeatherObject.getInfo());
        existingWeatherObject.setType(updatedWeatherObject.getType());
    }

    static WeatherObject unwrapWeatherObject(Optional<WeatherObject> entity, UUID id) {
        if (entity.isPresent()) {
            return entity.get();
        } else {
            throw new EntityNotFoundException(id, WeatherObject.class);
        }
    }
}
