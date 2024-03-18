package com.of.fishapp.dto;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Weather {

    private double temp;
    private double feelsLike;
    private double humidity;
    private double pressure;
    private double windSpeed;
    private double windDirection;
}
