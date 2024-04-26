package com.of.fishapp.client;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.exception.WeatherFetchException;
import com.of.fishapp.mapper.WeatherDataMapper;

import io.github.cdimascio.dotenv.Dotenv;

@Component
public class WeatherApiClient {

    private WeatherDataMapper weatherDataMapper = new WeatherDataMapper();

    private static final String UNITS = "metric";
    private static final String API_URL = "https://api.openweathermap.org/data/2.5/";

    Dotenv dotenv = Dotenv.load();
    private final String API_KEY = dotenv.get("WEATHER_API_KEY");

    public WeatherObject fetchWeatherData(Geolocation location, String type) {

        if (location != null) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String url = API_URL + type + "?lat=" + location.getLat() + "&lon=" + location.getLng() + "&appid="
                        + API_KEY + "&units=" + UNITS;
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    String jsonData = response.getBody();
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> data = mapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {
                    });

                    WeatherObject weather = weatherDataMapper.createWeatherObject(data, location, type);
                    return weather;
                }

            } catch (Exception e) {
                throw new WeatherFetchException("Failed to fetch weather data", e);
            }
        }
        return null;
    }
}
