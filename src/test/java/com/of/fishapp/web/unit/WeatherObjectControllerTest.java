package com.of.fishapp.web.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.mockito.Mockito.when;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.api.client.auth.openidconnect.IdToken;
import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.service.FirebaseAuthenticator;
import com.of.fishapp.service.WeatherObjectService;
import com.of.fishapp.web.WeatherObjectController;

@SpringBootTest
public class WeatherObjectControllerTest {

    @Mock
    private WeatherObjectService weatherObjectService;

    @Mock
    private FirebaseAuthenticator mockAuthenticator;

    @InjectMocks
    private WeatherObjectController controller;

    //private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
      /*   this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApplicationExceptionHandler())
                .build(); */
    }



}
