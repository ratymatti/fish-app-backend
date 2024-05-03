package com.of.fishapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
import com.of.fishapp.exception.EntityNotFoundException;
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

    @Test
    void getFishById_throwsExceptionWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fishService.getFishById(null));
    }

    @Test
    @SuppressWarnings("null")
    void getFishById_deletesFish() {
        User user = new User();
        UUID fishId = UUID.randomUUID();
        Fish deletedFish = createFish(user);

        when(fishRepository.findById(fishId)).thenReturn(Optional.of(deletedFish));
        fishService.deleteFish(fishId);

        verify(fishRepository).deleteById(fishId);

        when(fishRepository.findById(fishId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> fishService.getFishById(fishId));
    }

    @Test
    void deleteFish_throwsExceptionWhenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fishService.deleteFish(null));
    }

    @Test
    void updateFish_throwsExceptionWhenFishIsNull() {
        assertThrows(IllegalArgumentException.class, () -> fishService.updateFish(null));
    }

    @SuppressWarnings("null")
    @Test
    void updateFish_returnsFish() {
        User user = new User();
        Fish originalFish = createFish(user);

        originalFish.setSpecies("Hauki");
        originalFish.setLength(100);

        when(fishRepository.save(any(Fish.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fish updatedFish = fishService.updateFish(originalFish);

        assertEquals("Hauki", updatedFish.getSpecies());
        assertEquals(100, updatedFish.getLength());

        verify(fishRepository).save(updatedFish);
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
        fish.setLocation("Test");
        return fish;
    }
}
