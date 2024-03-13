package com.of.fishapp.web;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
        MockitoAnnotations.openMocks(this);

        User user = new User();
        user.setUsername("test");
        when(userService.getUser(1L)).thenReturn(user);

        ResponseEntity<String> response = controller.findById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("test", response.getBody());
    }

    @Test
    void createUser_returnsCreated() {
        MockitoAnnotations.openMocks(this);

        User user = new User();
        when(userService.saveUser(user)).thenReturn(user);

        ResponseEntity<User> response = controller.createUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void findById_returnsNotFound_whenUserNotFound() throws Exception {
        when(userService.getUser(any(Long.class))).thenThrow(new EntityNotFoundException(1L, User.class));

        MvcResult mvcResult = mockMvc.perform(get("/1"))
                .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }
}