package com.of.fishapp.client;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.dto.Weather;
import com.of.fishapp.dto.WeatherInfo;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.mapper.WeatherDataMapper;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WeatherApiClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private WeatherDataMapper weatherDataMapper;

    @InjectMocks
    private WeatherApiClient weatherApiClient;

    @SuppressWarnings("null")
    @Test
    public void testFetchWeatherData() {
        // Arrange
        Geolocation location = new Geolocation(65.96667, 29.18333);
        String type = "weather";
        UUID userId = UUID.randomUUID();
        String userIdString = userId.toString();
        String responseJson = "{\n" + //
                        "    \"id\": \"a275e159-ecf5-48cf-917e-c89c951c27d1\",\n" + //
                        "    \"userId\": \""+ userIdString +"\",\n" + //
                        "    \"type\": \"weather\",\n" + //
                        "    \"name\": \"Kuusamo\",\n" + //
                        "    \"info\": \"clear sky\",\n" + //
                        "    \"coords\": {\n" + //
                        "        \"lat\": 65.96667,\n" + //
                        "        \"lng\": 29.18333\n" + //
                        "    },\n" + //
                        "    \"forecastArray\": null,\n" + //
                        "    \"currentWeather\": {\n" + //
                        "        \"icon\": \"01d\",\n" + //
                        "        \"time\": \"17:05 18/03/2024\",\n" + //
                        "        \"weather\": {\n" + //
                        "            \"temp\": -1.98,\n" + //
                        "            \"feelsLike\": -8.05,\n" + //
                        "            \"humidity\": 26.0,\n" + //
                        "            \"pressure\": 1018.0,\n" + //
                        "            \"windSpeed\": 6.17,\n" + //
                        "            \"windDirection\": 230.0\n" + //
                        "        }\n" + //
                        "    }\n" + //
                        "}"; 
        ResponseEntity<Object> responseEntity = new ResponseEntity<>(responseJson, HttpStatus.OK);
        
        WeatherObject expectedWeatherObject = new WeatherObject();
        expectedWeatherObject.setId(UUID.fromString("a275e159-ecf5-48cf-917e-c89c951c27d1"));
        expectedWeatherObject.setUserId(UUID.fromString(userIdString));
        expectedWeatherObject.setType("weather");
        expectedWeatherObject.setName("Kuusamo");
        expectedWeatherObject.setInfo("clear sky");

        Geolocation coords = new Geolocation();
        coords.setLat(65.96667);
        coords.setLng(29.18333);
        expectedWeatherObject.setCoords(coords);

        WeatherInfo currentWeather = new WeatherInfo();
        currentWeather.setIcon("01d");
        currentWeather.setTime("17:05 18/03/2024");

        Weather weather = new Weather();
        weather.setTemp(-1.98);
        weather.setFeelsLike(-8.05);
        weather.setHumidity(26.0);
        weather.setPressure(1018.0);
        weather.setWindSpeed(6.17);
        weather.setWindDirection(230.0);
        currentWeather.setWeather(weather);

        expectedWeatherObject.setCurrentWeather(currentWeather);

        lenient().when(restTemplate.getForEntity(anyString(), any())).thenReturn(responseEntity);
        when(weatherDataMapper.createWeatherObject(any(), any(), any(), any())).thenReturn(expectedWeatherObject);

        // Act
        WeatherObject actualWeatherObject = weatherApiClient.fetchWeatherData(location, type, userId);

        // Assert
        assertEquals(expectedWeatherObject, actualWeatherObject);
    }
}