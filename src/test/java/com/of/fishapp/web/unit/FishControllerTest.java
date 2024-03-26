package com.of.fishapp.web.unit;
/*
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

import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.of.fishapp.ApplicationExceptionHandler;

import com.of.fishapp.entity.Fish;

import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.FishService;
import com.of.fishapp.service.UserService;
import com.of.fishapp.web.FishController;

@SpringBootTest
public class FishControllerTest {

    @Mock
    private FishService fishService;

    @Mock
    private UserService userService;

    @InjectMocks
    private FishController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApplicationExceptionHandler())
                .build();
    }


    private Fish createFish(User user) {
        Fish fish = new Fish();
        fish.setUser(user);
        Geolocation geolocation = new Geolocation();
        fish.setGeolocation(geolocation);
        fish.setSpecies("Turska");
        UUID fishId = UUID.randomUUID();
        fish.setId(fishId);
        fish.setLength(101);
        fish.setLocationName("Test");
        return fish;
    }
}*/
