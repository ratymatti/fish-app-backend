package com.of.fishapp.web.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.of.fishapp.ApplicationExceptionHandler;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.FirebaseAuthenticator;
import com.of.fishapp.service.UserService;
import com.of.fishapp.service.WeatherObjectService;
import com.of.fishapp.web.WeatherObjectController;

@SpringBootTest
public class WeatherObjectControllerIntegrationTest {

    @Mock
    private WeatherObjectService weatherObjectService;

    @Mock
    private UserService userService;

    @Mock
    private FirebaseAuthenticator authenticator;

    @InjectMocks
    private WeatherObjectController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(new WeatherObjectController(weatherObjectService, userService, authenticator))
                .setControllerAdvice(new ApplicationExceptionHandler())
                .build();
    }

    @Test
    void findById_returnsNotFound() throws Exception {
        UUID weatherObjectId = UUID.randomUUID();
        when(weatherObjectService.getWeatherObject(weatherObjectId))
                .thenThrow(new EntityNotFoundException(weatherObjectId, WeatherObject.class));

        MvcResult mvcResult = mockMvc.perform(get("/weather/" + weatherObjectId)).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void findById_returnsWeatherObject() throws Exception {
        WeatherObject weatherObject = new WeatherObject();
        UUID weatherObjectId = UUID.randomUUID();
        weatherObject.setId(weatherObjectId);
        weatherObject.setName("test");

        when(weatherObjectService.getWeatherObject(weatherObjectId)).thenReturn(weatherObject);

        MvcResult mvcResult = mockMvc.perform(get("/weather/" + weatherObjectId))
            .andExpect(status().isOk())
            .andReturn();

        verify(weatherObjectService).getWeatherObject(weatherObjectId);    

        String responseBody = mvcResult.getResponse().getContentAsString();
        WeatherObject returnedWeatherObject = new ObjectMapper().readValue(responseBody, WeatherObject.class);

        assertEquals(weatherObject.getId(), returnedWeatherObject.getId());
        assertEquals(weatherObject.getName(), returnedWeatherObject.getName());
    }
}
