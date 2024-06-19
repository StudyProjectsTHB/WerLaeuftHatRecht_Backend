package com.example.mux.weather.controller;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
@NoArgsConstructor
public class WeatherController {

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping
    public ResponseEntity<Map<String, Object>> getWeather() {
        // PROTOTYP = ist schon okay so...
        String weatherApi = "https://api.openweathermap.org/data/2.5/forecast?lat=52.41667&lon=12.55&dt={time}&appid=8d1f98d01132620b41e61cf49ba5c565&units=metric";
        String url = weatherApi.replace("{time}", Long.toString(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.ofHours(0))));
        Map<String, Object> response;
        Map<String, Object> result = null;
        try {
            response = restTemplate.getForObject(url, Map.class);
            System.out.println(response);
            List<Map<String, Object>> forecasts = (List<Map<String, Object>>) response.get("list");
            LocalDate today = LocalDate.now();
            LocalDate tomorrow = today.plusDays(1);
            LocalDate tomorrow2 = today.plusDays(2);

            // Wetterbedingungen für heute und morgen extrahieren
            Map<String, Object> todayWeather = extractWeatherForDate(forecasts, today);
            Map<String, Object> tomorrowWeather = extractWeatherForDate(forecasts, tomorrow);
            Map<String, Object> tomorrowWeather2 = extractWeatherForDate(forecasts, tomorrow2);

            // Erstelle eine Antwort-Map
            result = new HashMap<>();
            result.put("today", todayWeather);
            result.put("tomorrow", tomorrowWeather);
            result.put("day_after_tomorrow2", tomorrowWeather2);
        }
        catch (Exception e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            System.err.println(e);
        }
        System.out.println(result);
        return ResponseEntity.ok(result);
    }

    private Map<String, Object> extractWeatherForDate(List<Map<String, Object>> forecasts, LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Filtere die Vorhersagen für das gegebene Datum
        List<Map<String, Object>> dailyForecasts = forecasts.stream()
                .filter(forecast -> ((String) forecast.get("dt_txt")).startsWith(date.format(formatter)))
                .collect(Collectors.toList());

        // Sammle Wetterbedingungen und finde die Höchsttemperatur
        Map<String, Long> weatherCounts = new HashMap<>();
        double maxTemp = Double.MIN_VALUE;

        for (Map<String, Object> forecast : dailyForecasts) {
            // Extrahiere die Wetterbedingungen
            List<Map<String, String>> weatherList = (List<Map<String, String>>) forecast.get("weather");
            String mainWeather = weatherList.get(0).get("main");

            // Zähle das Vorkommen der Wetterbedingungen
            weatherCounts.put(mainWeather, weatherCounts.getOrDefault(mainWeather, 0L) + 1);

            // Finde die Höchsttemperatur des Tages
            Map<String, Double> main = (Map<String, Double>) forecast.get("main");
            double tempMax = main.get("temp_max");
            if (tempMax > maxTemp) {
                maxTemp = tempMax;
            }
        }

        // Bestimme die häufigste Wetterbedingung
        String mostFrequentWeather = weatherCounts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown");

        // Setze Wetterbedingungen und Höchsttemperatur in die Rückgabe-Map
        Map<String, Object> weatherInfo = new HashMap<>();
        weatherInfo.put("weather", translateWeather(mostFrequentWeather));
        weatherInfo.put("maxTemperature", Math.round(maxTemp));

        return weatherInfo;
    }

    private String translateWeather(String weather) {
        // Übersetze Wetterbedingungen in Deutsch
        switch (weather.toLowerCase()) {
            case "clear":
                return "sonnig";
            case "clouds":
                return "bewölkt";
            case "rain":
                return "regen";
            case "snow":
                return "schnee";
            case "drizzle":
                return "nieselregen";
            case "thunderstorm":
                return "gewitter";
            case "fog":
            case "mist":
            case "haze":
                return "nebelig";
            default:
                return weather; // unveränderte Rückgabe, wenn keine Übersetzung vorhanden ist
        }
    }
}