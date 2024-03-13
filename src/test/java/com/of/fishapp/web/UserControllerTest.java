package com.of.fishapp.web;

import com.of.fishapp.entity.User;
import com.of.fishapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController controller;

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
}