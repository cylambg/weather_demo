package com.demo.application.service;

import com.demo.application.dto.WeatherResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class WeatherService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${weatherstack.url}")
    String weatherstackUrl;

    @Value("${weatherstack.access_key}")
    String weatherstackAccessKey;

    @Value("${openweathermap.url}")
    String openweathermapUrl;

    @Value("${openweathermap.api_id}")
    String openweathermapApiId;

    @Cacheable(value="weather")
    public WeatherResponse getWeather(String city) {
        return Optional.ofNullable(getWeatherFromWeatherstack(city)).orElseGet(() -> getWeatherFromOpenweathermap(city));
    }

    @CacheEvict(value = "weather", allEntries = true)
    @Scheduled(fixedDelay = 3 * 1000)
    public void weatherCacheEvict() {
    }

    WeatherResponse getWeatherFromWeatherstack(String city) {
        try {
            log.info("trying to get weather from weatherstack");
            WeatherResponse response = new WeatherResponse();
            Map<String, Object> apiResult = restTemplate.getForObject(weatherstackUrl, Map.class, city, weatherstackAccessKey);
            Integer temperatureDegrees = (Integer) ((Map<String, Object>) apiResult.get("current")).get("temperature");
            Integer windSpeed = (Integer) ((Map<String, Object>) apiResult.get("current")).get("wind_speed");
            response.setTemperatureDegrees(temperatureDegrees);
            response.setWindSpeed(windSpeed);
            return response;
        } catch (Exception e) {
            log.error("error when getting weather from weatherstack");
            e.printStackTrace();
            return null;
        }
    }

    WeatherResponse getWeatherFromOpenweathermap(String city) {
        try {
            log.info("trying to get weather from openweathermap");
            WeatherResponse response = new WeatherResponse();
            Map<String, Object> apiResult = restTemplate.getForObject(openweathermapUrl, Map.class, city, openweathermapApiId);
            Integer temperatureDegrees = ((Long) Math.round((Double) ((Map<String, Object>) apiResult.get("main")).get("temp"))).intValue();
            Integer windSpeed = ((Long) Math.round((Double) ((Map<String, Object>) apiResult.get("wind")).get("speed"))).intValue();
            response.setTemperatureDegrees(temperatureDegrees);
            response.setWindSpeed(windSpeed);
            return response;
        } catch (Exception e) {
            log.error("error when getting weather from openweathermap");
            e.printStackTrace();
            return null;
        }
    }
}
