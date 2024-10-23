package com.openWeather.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openWeather.demo.model.WeatherData;
import org.apache.camel.ProducerTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WeatherService {

    private final ProducerTemplate producerTemplate;
    public WeatherService(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public String getWeatherData(String city) throws JsonProcessingException {
        String response = producerTemplate.requestBody("direct:weatherRoute", city, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response);

        WeatherData weatherData = new WeatherData();
        weatherData.setCity(root.get("name").asText());
        weatherData.setTemperature(root.get("main").get("temp").asDouble());
        weatherData.setHumidity(root.get("main").get("humidity").asDouble());
        weatherData.setDescription(root.get("weather").get(0).get("description").asText());
        weatherData.setTimestamp(LocalDateTime.now());

        // Save weather data to database
        producerTemplate.sendBody("jpa:com.openWeather.demo.model.WeatherData", weatherData);

        return response;
    }
}
