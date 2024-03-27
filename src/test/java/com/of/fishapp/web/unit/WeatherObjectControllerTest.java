package com.of.fishapp.web.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.WeatherObjectService;
import com.of.fishapp.web.WeatherObjectController;

@SpringBootTest
public class WeatherObjectControllerTest {

    @Mock
    private WeatherObjectService weatherObjectService;

    @InjectMocks
    private WeatherObjectController controller;

    //private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
      /*   this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ApplicationExceptionHandler())
                .build(); */
    }

    @Test
    void findById_returnsWeatherObject() {
        WeatherObject weatherObject = new WeatherObject();
        UUID weatherObjectId = UUID.randomUUID();
        when(weatherObjectService.getWeatherObject(weatherObjectId)).thenReturn(weatherObject);

        ResponseEntity<WeatherObject> response = controller.findById(weatherObjectId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(weatherObject, response.getBody());
    }

    @Test
    void findById_returnsNotFound() {
        UUID weatherObjectId = UUID.randomUUID();
        when(weatherObjectService.getWeatherObject(weatherObjectId))
                .thenThrow(new EntityNotFoundException(weatherObjectId, WeatherObject.class));

        assertThrows(EntityNotFoundException.class, () -> {
            controller.findById(weatherObjectId);
        });
    }


    @Test
    void saveWeatherObject_returnsWeatherObject() {
        WeatherObject weatherObject = new WeatherObject();
        when(weatherObjectService.saveWeatherObject(weatherObject)).thenReturn(weatherObject);

        ResponseEntity<WeatherObject> response = controller.saveWeatherObject(weatherObject);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(weatherObject, response.getBody());
    }


}
