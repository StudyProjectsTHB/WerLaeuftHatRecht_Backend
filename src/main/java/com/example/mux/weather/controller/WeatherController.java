package com.example.mux.weather.controller;

import com.example.mux.weather.service.WeatherService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/weather")
@AllArgsConstructor
public class WeatherController {
    private WeatherService weatherService;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getWeather() {
        try {
            Map<String, Object> result = weatherService.getWeather(LocalDate.now());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new HashMap<>());
        }
    }
}