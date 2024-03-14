package com.of.fishapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.of.fishapp.entity.Location;
import com.of.fishapp.repository.LocationRepository;

@SpringBootTest
public class LocationServiceImplTest {

    @InjectMocks
    private LocationServiceImpl locationsService;

    @Mock
    private LocationRepository locationRepository;

    @SuppressWarnings("null")
    @Test
    public void testSaveLocation() {
        Location location = new Location();
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        Location savedLocation = locationsService.saveLocation(location);

        assertNotNull(savedLocation);
        verify(locationRepository, times(1)).save(location);
    }
}