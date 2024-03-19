package com.of.fishapp.web.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.of.fishapp.ApplicationExceptionHandler;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.WeatherObjectService;
import com.of.fishapp.web.WeatherObjectController;

public class WeatherObjectControllerTest {

    @Mock
    private WeatherObjectService weatherObjectService;

    @InjectMocks
    private WeatherObjectController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApplicationExceptionHandler())
                .build();
    }

    @Test
    void findById_returnsWeatherObject() {
        WeatherObject weatherObject = new WeatherObject();
        UUID weatherObjectId = UUID.randomUUID();
        when(weatherObjectService.getWeatherObject(weatherObjectId)).thenReturn(weatherObject);

        ResponseEntity<WeatherObject> response = controller.findById(weatherObjectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(weatherObject, response.getBody());
    }

    @Test
    void findById_returnsNotFound() {
        UUID weatherObjectId = UUID.randomUUID();
        when(weatherObjectService.getWeatherObject(weatherObjectId))
                .thenThrow(new EntityNotFoundException(weatherObjectId, WeatherObject.class));

        assertThrows(EntityNotFoundException.class, () -> {
            controller.findById(weatherObjectId);
        });
    }

    @Test
    void getWeatherObjectsByUserId_returnsWeatherObjects() {
        WeatherObject weatherObject = new WeatherObject();

        UUID userId = UUID.randomUUID();
        weatherObject.setUserId(userId);
        when(weatherObjectService.getWeatherObjects(userId)).thenReturn(List.of(weatherObject));

        ResponseEntity<List<WeatherObject>> response = controller.getWeatherObjectsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(weatherObject), response.getBody());
    }

    @Test
    void saveWeatherObject_returnsWeatherObject() {
        WeatherObject weatherObject = new WeatherObject();
        when(weatherObjectService.saveWeatherObject(weatherObject)).thenReturn(weatherObject);

        ResponseEntity<WeatherObject> response = controller.saveWeatherObject(weatherObject);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(weatherObject, response.getBody());
    }

    @Test
    void getAndSaveWeather_returnsWeatherObject() throws Exception {
        UUID userId = UUID.randomUUID();
        double lat = 65.96667;
        double lng = 29.18333;
        WeatherObject expectedWeatherObject = new WeatherObject();
        expectedWeatherObject.setUserId(userId);
        when(weatherObjectService.fetchAndSaveWeatherData(eq(userId), any(Geolocation.class))).thenReturn(expectedWeatherObject);

        MvcResult mvcResult = mockMvc.perform(post("/weather/" + userId + "/" + lat + "/" + lng))
            .andExpect(status().isCreated())
            .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        WeatherObject returnedWeatherObject = new ObjectMapper().readValue(responseBody, WeatherObject.class);
        
        assertEquals(expectedWeatherObject.getId(), returnedWeatherObject.getId());
        assertEquals(expectedWeatherObject.getUserId(), returnedWeatherObject.getUserId());
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
    }
}
