package com.of.fishapp.web;

import com.of.fishapp.entity.Location;
import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.UserService;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void findById_returnsUser() {

        User user = new User();
        user.setUsername("test");
        UUID userId = UUID.randomUUID();
        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<String> response = controller.findById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody());
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
}