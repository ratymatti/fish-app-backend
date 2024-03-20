package com.of.fishapp.web.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.of.fishapp.ApplicationExceptionHandler;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.Fish;
import com.of.fishapp.entity.Location;
import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.UserService;
import com.of.fishapp.web.UserController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.core.type.TypeReference;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApplicationExceptionHandler())
                .build();
    }

    @SuppressWarnings("null")
    @Test
    void findById_returnsUser() {

        User user = new User();
        user.setUsername("test");
        UUID userId = UUID.randomUUID();
        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<User> response = controller.findById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user.getUsername(), response.getBody().getUsername());
    }

    @Test
    void createUser_returnsCreated() {

        User user = new User();
        when(userService.saveUser(user)).thenReturn(user);

        ResponseEntity<User> response = controller.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void findById_returnsNotFound_whenUserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.getUser(any(UUID.class))).thenThrow(new EntityNotFoundException(userId, User.class));

        MvcResult mvcResult = mockMvc.perform(get("/" + userId.toString()))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void getLocationsByUserId_returnsLocations() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername("test");

        List<Location> locations = List.of(new Location());
        user.setLocations(locations);

        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<List<Location>> response = controller.getLocationsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locations, response.getBody());
    }

    @Test
    void getLocationsByUserId_returnsNotFound_whenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userService.getUser(userId)).thenThrow(new EntityNotFoundException(userId, User.class));

        assertThrows(EntityNotFoundException.class, () -> {
            controller.getLocationsByUserId(userId);
        });
    }

    @SuppressWarnings("null")
    @Test
    void getLocationsByUserId_returnsEmptyList_whenUserHasNoLocations() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setUsername("test");
        user.setLocations(new ArrayList<>());

        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<List<Location>> response = controller.getLocationsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getFishesByUserId_returnsFishes() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);

        Fish fish = createFish(user);

        List<Fish> expectedFishes = List.of(fish);
        user.setFishes(expectedFishes);

        when(userService.getUser(userId)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get("/user/" + userId.toString() + "/fishes"))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService).getUser(userId);        

        String responseBody = mvcResult.getResponse().getContentAsString();
        List<Fish> returnedFishes = new ObjectMapper().readValue(responseBody, new TypeReference<List<Fish>>() {});

        assertEquals(expectedFishes.size(), returnedFishes.size());
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(expectedFishes.get(0).getId(), returnedFishes.get(0).getId());
        assertEquals(expectedFishes.get(0).getSpecies(), returnedFishes.get(0).getSpecies());
    }

    @Test
    void getFishesByUserId_returnsNotFound_whenUserNotFound() throws Exception {
        UUID userId = UUID.randomUUID();
        when(userService.getUser(userId)).thenThrow(new EntityNotFoundException(userId, User.class));

        mockMvc.perform(get("/user/" + userId.toString() + "/fishes"))
                .andExpect(status().isNotFound());

        verify(userService).getUser(userId);
    }
    
    @Test
    void getFishesByUserId_returnsEmptyList_whenUserHasNoFishes() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setFishes(new ArrayList<>());

        when(userService.getUser(userId)).thenReturn(user);

        MvcResult mvcResult = mockMvc.perform(get("/user/" + userId.toString() + "/fishes"))
                .andExpect(status().isOk())
                .andReturn();
        
        verify(userService).getUser(userId);

        String responseBody = mvcResult.getResponse().getContentAsString();
        List<Fish> returnedFishes = new ObjectMapper().readValue(responseBody, new TypeReference<List<Fish>>() {});

        assertTrue(returnedFishes.isEmpty());
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
}