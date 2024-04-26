package com.of.fishapp.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.auth.FirebaseAuthException;
import com.of.fishapp.dto.IdToken;
import com.of.fishapp.entity.Fish;
import com.of.fishapp.entity.User;
import com.of.fishapp.entity.WeatherObject;
import com.of.fishapp.exception.EntityNotFoundException;
import com.of.fishapp.service.FirebaseAuthenticator;
import com.of.fishapp.service.FishService;
import com.of.fishapp.service.UserService;
import com.of.fishapp.service.WeatherObjectService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static com.of.fishapp.util.IdTokenUtil.removeBearerPrefix;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@AllArgsConstructor
@RequestMapping("/fish")
public class FishController {

    FishService fishService;
    UserService userService;
    WeatherObjectService weatherObjectService;
    FirebaseAuthenticator authenticator;

    @PostMapping("/save")
    public ResponseEntity<Fish> saveFish(@Valid @RequestBody Fish fish,
            @RequestHeader("Authorization") IdToken idToken) {
        try {
            removeBearerPrefix(idToken);
            if (!authenticator.verifyIdToken(idToken)) {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            String googleId = authenticator.getUidFromToken(idToken);
            User user = userService.getUserByGoogleId(googleId);

            if (user == null) {
                throw new EntityNotFoundException(User.class);
            }

            fish.setUser(user);

            if (isFishDateToday(fish)) {
                WeatherObject weatherObject = weatherObjectService.fetchAndSaveWeatherDataForFish(fish);
                fish.setWeather(weatherObject);
                fish = fishService.saveFish(fish);
            }

            Fish savedFish = fishService.saveFish(fish);
            return new ResponseEntity<>(savedFish, HttpStatus.CREATED);

        } catch (FirebaseAuthException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete/{fishId}")
    public ResponseEntity<Void> deleteFish(@PathVariable UUID fishId) {
        fishService.deleteFish(fishId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update/{fishId}")
    public ResponseEntity<Fish> updateFish(@PathVariable UUID fishId, @Valid @RequestBody Fish fish) {
        Fish updatedFish = fishService.updateFish(fish);
        return new ResponseEntity<>(updatedFish, HttpStatus.OK);
    }

    public boolean isFishDateToday(Fish fish) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        ZonedDateTime fishDateTime = ZonedDateTime.parse(fish.getDate(), formatter);
        LocalDate fishDate = fishDateTime.withZoneSameInstant(ZoneId.systemDefault()).toLocalDate();
        LocalDate today = LocalDate.now(ZoneId.systemDefault());
        return fishDate.equals(today);
    }

}
