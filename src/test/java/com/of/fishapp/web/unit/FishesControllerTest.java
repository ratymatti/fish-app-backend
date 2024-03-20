package com.of.fishapp.web.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.of.fishapp.ApplicationExceptionHandler;
import com.of.fishapp.entity.Fish;
import com.of.fishapp.service.FishService;
import com.of.fishapp.web.FishController;

public class FishesControllerTest {
    
    @Mock
    private FishService fishService;

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
    void findById_returnsNotFound() {
        UUID fishId = UUID.randomUUID();
        when(fishService.getFishById(fishId)).thenReturn(Optional.empty());

        ResponseEntity<Optional<Fish>> response = controller.findById(fishId);

        verify(fishService).getFishById(fishId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
