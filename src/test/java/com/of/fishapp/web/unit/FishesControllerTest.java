package com.of.fishapp.web.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.of.fishapp.ApplicationExceptionHandler;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.Fish;
import com.of.fishapp.entity.User;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.FishService;
import com.of.fishapp.service.UserService;
import com.of.fishapp.web.FishController;

public class FishesControllerTest {

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

    @SuppressWarnings("null")
    @Test
    void findById_returnsFish() {
        Fish expectedFish = new Fish();
        UUID fishId = UUID.randomUUID();

        expectedFish.setId(fishId);

        when(fishService.getFishById(fishId)).thenReturn(Optional.of(expectedFish));

        ResponseEntity<Optional<Fish>> response = controller.findById(fishId);

        Fish returnedFish = response.getBody().get();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedFish.getId(), returnedFish.getId());
    }

    @Test
    void findById_returnsNotFound() throws Exception {
        UUID fishId = UUID.randomUUID();
        when(fishService.getFishById(fishId)).thenThrow(new EntityNotFoundException(fishId, Fish.class));

        MvcResult mvcResult = mockMvc.perform(get("/fish/" + fishId.toString()))
                .andReturn();

        verify(fishService).getFishById(fishId);

        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    void saveFish_returnsCreated() throws Exception {
        User user = new User();
        UUID userId = UUID.randomUUID();
        user.setId(userId);

        Fish expectedFish = createFish(user);

        ObjectMapper objectMapper = new ObjectMapper();
        String fishJson = objectMapper.writeValueAsString(expectedFish);


        when(userService.getUser(userId)).thenReturn(user);
        when(fishService.saveFish(any(Fish.class))).thenReturn(expectedFish);

        @SuppressWarnings("null")
        MvcResult mvcResult = mockMvc.perform(post("/fish/save/" + userId.toString())
            .contentType(MediaType.APPLICATION_JSON)
            .content(fishJson))
            .andExpect(status().isCreated())
            .andReturn();
 
        
        verify(userService).getUser(userId);        
        verify(fishService).saveFish(any(Fish.class));

        String responseBody = mvcResult.getResponse().getContentAsString();
        Fish returnedFish = objectMapper.readValue(responseBody, Fish.class);

        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertEquals(expectedFish.getId(), returnedFish.getId());
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
