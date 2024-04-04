package com.of.fishapp.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.ErrorCode;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuthException;
import com.of.fishapp.ApplicationExceptionHandler;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.dto.IdToken;
import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.service.FirebaseAuthenticator;
import com.of.fishapp.service.WeatherObjectService;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class WeatherObjectControllerTest {

    @Mock
    private WeatherObjectService weatherObjectService;

    @Mock
    private FirebaseAuthenticator mockAuthenticator;

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
    void fetchAndSaveWeather_returnsWeatherObject() throws Exception {
        // Arrange
        User user = new User();
        Geolocation location = new Geolocation();
        IdToken idToken = new IdToken();
        WeatherObject expectedWeatherObject = new WeatherObject();

        when(mockAuthenticator.validateUser(any(IdToken.class))).thenReturn(user);
        when(weatherObjectService.fetchAndSaveWeatherData(eq(user), any(Geolocation.class)))
                .thenReturn(expectedWeatherObject);
        // Convert objects to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String locationJson = objectMapper.writeValueAsString(location);
        String expectedResponseJson = objectMapper.writeValueAsString(expectedWeatherObject);

        // Act and Assert
        mockMvc.perform(post("/weather/fetch/tracking")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", idToken)
                .content(locationJson))
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponseJson));

        verify(mockAuthenticator).validateUser(any(IdToken.class));
        verify(weatherObjectService).fetchAndSaveWeatherData(eq(user), any(Geolocation.class));
    }

    @Test
    void fetchAndSaveWeather_throwsException() throws Exception {
        // Arrange
        IdToken idToken = new IdToken();
        idToken.setIdToken("invalidToken");

        FirebaseException baseException = new FirebaseException(
            ErrorCode.UNKNOWN,
            "Test error message",
            null,
            null
        );
        doThrow(new FirebaseAuthException(baseException)).when(mockAuthenticator).validateUser(any(IdToken.class));

        // Act and Assert
        mockMvc.perform(post("/weather/fetch/tracking")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", idToken)
                .content("{}")) 
            .andExpect(status().isUnauthorized()); // Check for an unauthorized status
    }

    @Test
    void fetchCurrentWeather_returnsWeatherObject() throws Exception {
        // Arrange
        User user = new User();
        Geolocation location = new Geolocation();
        IdToken idToken = new IdToken();
        WeatherObject expectedWeatherObject = new WeatherObject();

        when(mockAuthenticator.validateUser(any(IdToken.class))).thenReturn(user);
        when(weatherObjectService.fetchCurrentWeather(eq(user), any(Geolocation.class)))
                .thenReturn(expectedWeatherObject);
        // Convert objects to JSON
        ObjectMapper objectMapper = new ObjectMapper();
        String locationJson = objectMapper.writeValueAsString(location);
        String expectedResponseJson = objectMapper.writeValueAsString(expectedWeatherObject);

        // Act and Assert
        mockMvc.perform(post("/weather/fetch/current")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", idToken)
                .content(locationJson))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponseJson));

        verify(mockAuthenticator).validateUser(any(IdToken.class));
        verify(weatherObjectService).fetchCurrentWeather(eq(user), any(Geolocation.class));
    }

    @Test
    void fetchCurrentWeather_throwsException() throws Exception {
        // Arrange
        IdToken idToken = new IdToken();
        idToken.setIdToken("invalidToken");

        FirebaseException baseException = new FirebaseException(
            ErrorCode.UNKNOWN,
            "Test error message",
            null,
            null
        );
        doThrow(new FirebaseAuthException(baseException)).when(mockAuthenticator).validateUser(any(IdToken.class));

        // Act and Assert
        mockMvc.perform(post("/weather/fetch/current")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", idToken)
                .content("{}")) 
            .andExpect(status().isUnauthorized()); // Check for an unauthorized status
    }

    @Test
    void deleteWeatherObject_returnsNoContent() throws Exception {
        // Arrange
        User user = new User();
        IdToken idToken = new IdToken();
        WeatherObject weatherObject = new WeatherObject();
        UUID idToRemove = UUID.randomUUID();
        weatherObject.setId(idToRemove);

        when(mockAuthenticator.validateUser(any(IdToken.class))).thenReturn(user);
        // Act
        controller.deleteWeatherObject(weatherObject.getId(), idToken);

        // Assert
        verify(mockAuthenticator).validateUser(any(IdToken.class));
        verify(weatherObjectService).deleteWeatherObject(weatherObject.getId());
    }
}
