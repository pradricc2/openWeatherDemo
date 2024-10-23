package com.openWeather.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "weather_data")
@Data
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weather_seq")
    @SequenceGenerator(name = "weather_seq", sequenceName = "weather_sequence", allocationSize = 1)
    private Long id;

    private String city;
    private double temperature;
    private double humidity;
    private String description;
    private LocalDateTime timestamp;

}

