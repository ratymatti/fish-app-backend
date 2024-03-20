package com.of.fishapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.repository.WeatherObjectRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WeatherObjectServiceImplTest {
    
    @Mock
    private WeatherObjectRepository weatherObjectRepository;

    @InjectMocks
    private WeatherObjectServiceImpl weatherObjectService;

    @SuppressWarnings("null")
    @Test
    void getWeatherObject_returnsWeatherObject() {
        UUID id = UUID.randomUUID();
        WeatherObject expectedWeatherObject = new WeatherObject();

        when(weatherObjectRepository.findById(id)).thenReturn(Optional.of(expectedWeatherObject));

        WeatherObject actualWeatherObject = weatherObjectService.getWeatherObject(id);

        assertEquals(expectedWeatherObject, actualWeatherObject);
    }

    @Test
    void getWeatherObject_throwsExceptionWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> weatherObjectService.getWeatherObject(null));
    }

    @Test 
    void getWeatherObjects_returnsWeatherObjects() {
        UUID userId = UUID.randomUUID();
        WeatherObject weatherObject = new WeatherObject();
        when(weatherObjectRepository.findByUserId(userId)).thenReturn(List.of(weatherObject));

        List<WeatherObject> actualWeatherObjects = weatherObjectService.getWeatherObjects(userId);

        assertEquals(List.of(weatherObject), actualWeatherObjects);
    }

    @Test
    void getWeatherObjects_throwsExceptionWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> weatherObjectService.getWeatherObjects(null));
    }

    @Test
    void saveWeatherObject_returnsWeatherObject() {
        WeatherObject weatherObject = new WeatherObject();
        when(weatherObjectRepository.save(weatherObject)).thenReturn(weatherObject);

        WeatherObject actualWeatherObject = weatherObjectService.saveWeatherObject(weatherObject);

        assertEquals(weatherObject, actualWeatherObject);
    }

    @Test
    void saveWeatherObject_throwsExceptionWhenWeatherObjectIsNull() {
        assertThrows(IllegalArgumentException.class, () -> weatherObjectService.saveWeatherObject(null));
    }

}
