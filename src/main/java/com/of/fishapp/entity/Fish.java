package com.of.fishapp.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.of.fishapp.dto.Geolocation;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "fishes")
public class Fish {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @JsonManagedReference("fish-weather")
    @OneToOne
    @JoinColumn(name = "weather_id")
    @JsonSerialize
    private WeatherObject weather;

    @Embedded
    private Geolocation geolocation;

    @NotBlank
    @Nonnull
    @Column(nullable = false)
    private String species;

    @Min(1)
    private int length;

    @NotBlank
    @Nonnull
    private String location;

    private String comment;

    private String date;
}
