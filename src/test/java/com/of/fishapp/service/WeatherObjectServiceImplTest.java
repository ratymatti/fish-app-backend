package com.of.fishapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.repository.WeatherObjectRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class WeatherObjectServiceImplTest {
    
    @Mock
    private WeatherObjectRepository weatherObjectRepository;

    @InjectMocks
    private WeatherObjectServiceImpl weatherObjectService;


    @Test 
    void getWeatherObjects_returnsWeatherObjects() {
        User user = new User();
        WeatherObject weatherObject = new WeatherObject();
        when(weatherObjectRepository.findByUser(user)).thenReturn(List.of(weatherObject));

        List<WeatherObject> actualWeatherObjects = weatherObjectService.getWeatherObjects(user);

        assertEquals(List.of(weatherObject), actualWeatherObjects);
    }

    @Test
    void getWeatherObjects_throwsExceptionWhenUserIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> weatherObjectService.getWeatherObjects(null));
    }

}
