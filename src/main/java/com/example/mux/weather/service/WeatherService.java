package com.example.mux.weather.service;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
public class WeatherService {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.url}")
    private String baseUrl;

    @Value("${weather.api.key}")
    private String key;

    public Map<String, Object> getWeather(LocalDate localDate) {

        String weatherApi = baseUrl.replace("{key}", key);
        String url = weatherApi.replace("{time}", Long.toString(LocalDate.now().atStartOfDay().toEpochSecond(ZoneOffset.ofHours(0))));
        Map<String, Object> response;
        Map<String, Object> result;
        response = restTemplate.getForObject(url, Map.class);
        List<Map<String, Object>> forecasts = (List<Map<String, Object>>) response.get("list");
        LocalDate tomorrow = localDate.plusDays(1);
        LocalDate tomorrow2 = localDate.plusDays(2);

        // Wetterbedingungen für heute und morgen extrahieren
        Map<String, Object> todayWeather = extractWeatherForDate(forecasts, localDate);
        Map<String, Object> tomorrowWeather = extractWeatherForDate(forecasts, tomorrow);
        Map<String, Object> tomorrowWeather2 = extractWeatherForDate(forecasts, tomorrow2);

        // Erstelle eine Antwort-Map
        result = new HashMap<>();
        result.put("today", todayWeather);
        result.put("tomorrow", tomorrowWeather);
        result.put("day_after_tomorrow", tomorrowWeather2);

        return result;
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
//            Map<String, Double> main = (Map<String, Double>) forecast.get("main");

            Object mainObject = forecast.get("main");

            Map<String, Object> main = (Map<String, Object>) mainObject;

            double tempMax = 0;
            if (main.get("temp_max") instanceof Integer) {
                tempMax = ((Integer) main.get("temp_max")).doubleValue();
            } else if (main.get("temp_max") instanceof Double) {
                tempMax = (Double) main.get("temp_max");
            } else if (main.get("temp_max") instanceof Long) {
                tempMax = ((Long) main.get("temp_max")).doubleValue();
            } else if (main.get("temp_max") instanceof Float) {
                tempMax = ((Float) main.get("temp_max")).doubleValue();
            }

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
        return switch (weather.toLowerCase()) {
            case "clear" -> "sonnig";
            case "clouds" -> "bewölkt";
            case "rain" -> "regen";
            case "snow" -> "schnee";
            case "drizzle" -> "nieselregen";
            case "thunderstorm" -> "gewitter";
            case "fog", "mist", "haze" -> "nebelig";
            default -> weather; // unveränderte Rückgabe, wenn keine Übersetzung vorhanden ist
        };
    }
}
