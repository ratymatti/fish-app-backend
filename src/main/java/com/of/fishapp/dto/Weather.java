package com.of.fishapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Weather {

    private double temp;
    private double feels_like;
    private double humidity;
    private double pressure;
    private double wind_speed;
    private double wind_direction;
}
