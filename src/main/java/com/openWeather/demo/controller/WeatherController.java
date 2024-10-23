package com.openWeather.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.openWeather.demo.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {


    private final WeatherService weatherService;
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/weather/{city}")
    public ResponseEntity<String> getWeather(@PathVariable String city) throws JsonProcessingException {
        String response = weatherService.getWeatherData(city);
        return ResponseEntity.ok(response);
    }
}

