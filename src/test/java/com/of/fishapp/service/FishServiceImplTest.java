package com.of.fishapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.entity.Fish;
import com.of.fishapp.entity.User;
import com.of.fishapp.repository.FishRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class FishServiceImplTest {
    
    @Mock
    private FishRepository fishRepository;

    @InjectMocks
    private FishServiceImpl fishService;

    
    @SuppressWarnings("null")
    @Test
    void saveFish_returnsFish() {
        User user = new User();
        Fish expectedFish = createFish(user);

        when(fishRepository.save(expectedFish)).thenReturn(expectedFish);

        Fish actualFish = fishService.saveFish(expectedFish);

        assertEquals(expectedFish.getId(), actualFish.getId());
    }

    @Test
    void saveFish_throwsExceptionWhenFishIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fishService.saveFish(null));
    }

    @SuppressWarnings("null")
    @Test
    void getFishById_returnsFish() {
        User user = new User();
        Fish expectedFish = createFish(user);
        UUID fishId = expectedFish.getId();

        when(fishRepository.findById(fishId)).thenReturn(Optional.of(expectedFish));

        Fish actualFish = fishService.getFishById(fishId);

        assertEquals(expectedFish.getId(), actualFish.getId());
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
