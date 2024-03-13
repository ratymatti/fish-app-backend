package com.of.fishapp.service;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.of.fishapp.entity.User;
import com.of.fishapp.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void getUserById_returnsUser() {
        User expectedUser = new User();
        expectedUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUser(1L);

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void getUserByUsername_returnsUser() {
        User expectedUser = new User();
        expectedUser.setUsername("test");
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(expectedUser));

        User actualUser = userService.getUser("test");

        assertEquals(expectedUser, actualUser);
    }

    @Test
    void saveUser_throwsExceptionWhenPasswordIsNull() {
        User user = new User();

        assertThrows(IllegalArgumentException.class, () -> userService.saveUser(user));
    }

    @SuppressWarnings("null")
    @Test
    void saveUser_encodesPasswordAndSavesUser() {
        // Arrange
        User user = new User();
        user.setPassword("password");
        when(bCryptPasswordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userService.saveUser(user);

        // Assert
        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository).save(user);
    }
}