package com.of.fishapp.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.dto.Weather;
import com.of.fishapp.dto.WeatherInfo;
import com.of.fishapp.entity.WeatherObject;

public class WeatherDataMapper {
    
    @SuppressWarnings("unchecked")
    public  WeatherObject createWeatherObject(Map<String, Object> data, Geolocation location, String type, UUID userId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        String currentTime = LocalDateTime.now().format(formatter);
        

        WeatherObject weatherObject = new WeatherObject();

        // Set the fields of the weatherObject here
        weatherObject.setUserId(userId); // Replace with actual user ID
        weatherObject.setType(type);
        weatherObject.setName((String) data.get("name"));
        weatherObject.setInfo((String) ((Map<String, Object>) ((List<?>) data.get("weather")).get(0)).get("description"));
        weatherObject.setCoords(location);

        Map<String, Object> main = (Map<String, Object>) data.get("main");
        Weather weather = new Weather();
        weather.setTemp((Double) main.get("temp"));
        weather.setFeelsLike((Double) main.get("feels_like"));
        weather.setHumidity((Integer) main.get("humidity"));
        weather.setPressure((Integer) main.get("pressure"));

        Map<String, Object> wind = (Map<String, Object>) data.get("wind");
        weather.setWindSpeed((Double) wind.get("speed"));
        weather.setWindDirection((Integer) wind.get("deg"));

        WeatherInfo weatherInfo = new WeatherInfo();
        String icon = (String) ((Map<String, Object>) ((List<?>) data.get("weather")).get(0)).get("icon");
        weatherInfo.setIcon(icon);
        weatherInfo.setTime(currentTime);
        weatherInfo.setWeather(weather);

        weatherObject.setCurrentWeather(weatherInfo);

        return weatherObject;
    }
}
