package com.of.fishapp.entity;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.of.fishapp.constants.WeatherType;
import com.of.fishapp.dto.Geolocation;
import com.of.fishapp.dto.WeatherInfo;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WeatherObject {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    private WeatherType type;

    private String name;
    private String info;
    private Geolocation coords;

    @ElementCollection
    private List<WeatherInfo> forecastArray;

    @Embedded
    private WeatherInfo currentWeather;
    
}
