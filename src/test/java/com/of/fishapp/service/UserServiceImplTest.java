package com.of.fishapp.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.of.fishapp.entity.User;
import com.of.fishapp.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @SuppressWarnings("null")
    @Test
    void getUserById_returnsUser() {
        User expectedUser = new User();
        UUID userId = UUID.randomUUID();
        expectedUser.setId(userId);
        when(userRepository.findById(userId)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUser(userId);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByUsername_returnsUser() {
        User expectedUser = new User();
        expectedUser.setGoogleId("test");
        when(userRepository.findByGoogleId("test")).thenReturn(expectedUser);

        User actualUser = userService.getUserByGoogleId("test");

        assertEquals(expectedUser, actualUser);
    }

}