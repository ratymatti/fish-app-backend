package com.of.fishapp.web;

import com.google.firebase.auth.FirebaseAuthException;
import com.of.fishapp.dto.IdToken;

import com.of.fishapp.entity.Location;
import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.FirebaseAuthenticator;
import com.of.fishapp.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private FirebaseAuthenticator mockAuthenticator;

    @InjectMocks
    private UserController controller;

    //private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
       /*  this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApplicationExceptionHandler())
                .build();*/
    }

    @Test
    void authenticateUser_returnsOk_whenValidToken() throws FirebaseAuthException {
        IdToken idToken = new IdToken("validToken");
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);

        lenient().when(mockAuthenticator.verifyIdToken(any(IdToken.class))).thenReturn(true);
        lenient().when(mockAuthenticator.getUidFromToken(any(IdToken.class))).thenReturn("googleId"); // Stub getUidFromToken to return a non-null string

        when(mockAuthenticator.verifyIdToken(any(IdToken.class))).thenReturn(true);
        when(mockAuthenticator.getUidFromToken(any(IdToken.class))).thenReturn("googleId"); // Stub getUidFromToken to return a non-null string
        when(userService.getUserByGoogleId(anyString())).thenReturn(user);

        ResponseEntity<String> response = controller.authenticateUser(idToken);

        verify(mockAuthenticator).verifyIdToken(any(IdToken.class));
        verify(mockAuthenticator).getUidFromToken(any(IdToken.class));
        verify(userService).getUserByGoogleId(anyString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void authenticateUser_returnsUnauthorized_whenInvalidToken() {
        IdToken idToken = new IdToken("invalidToken");

        when(mockAuthenticator.verifyIdToken(any(IdToken.class))).thenReturn(false);

        ResponseEntity<String> response = controller.authenticateUser(idToken);

        verify(mockAuthenticator).verifyIdToken(any(IdToken.class));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void getLocationsByUserId_returnsLocations() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setName("Test User");

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

    @Test
    void getLocationsByUserId_returnsEmptyList_whenUserHasNoLocations() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setLocations(new ArrayList<>());

        when(userService.getUser(userId)).thenReturn(user);

        ResponseEntity<List<Location>> response = controller.getLocationsByUserId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }



/*     
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
    }*/
}