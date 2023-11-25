package com.demo.application.service;

import com.demo.application.dto.OpenweathermapResponse;
import com.demo.application.dto.WeatherResponse;
import com.demo.application.dto.WeatherstackResponse;
import com.demo.application.util.Common;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class WeatherService {

    RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${weatherstack.url}")
    String weatherstackUrl;

    @Value("${weatherstack.access_key}")
    String weatherstackAccessKey;

    @Value("${openweathermap.url}")
    String openweathermapUrl;

    @Value("${openweathermap.access_key}")
    String openweathermapAccessKey;

    @Cacheable(value="weather")
    public WeatherResponse getWeather(String city) {
        return Optional.ofNullable(getWeatherFromWeatherstack(city)).orElseGet(() -> getWeatherFromOpenweathermap(city));
    }

    @CacheEvict(value = "weather", allEntries = true)
    @Scheduled(fixedDelay = 60 * 1000)
    public void weatherCacheEvict() {
    }

    WeatherResponse getWeatherFromWeatherstack(String city) {
        try {
            log.info("trying to get weather from weatherstack for city {}", city);
            WeatherstackResponse apiResult = restTemplate.getForObject(weatherstackUrl, WeatherstackResponse.class, city, weatherstackAccessKey);
            return WeatherResponse.builder()
                    .temperatureDegrees(apiResult.getCurrent().getTemperature())
                    .windSpeed(apiResult.getCurrent().getWindSpeed()).build();
        } catch (Exception e) {
            log.error("error when getting weather from weatherstack", e);
            return null;
        }
    }

    WeatherResponse getWeatherFromOpenweathermap(String city) {
        try {
            log.info("trying to get weather from openweathermap for city {}", city);
            OpenweathermapResponse apiResult = restTemplate.getForObject(openweathermapUrl, OpenweathermapResponse.class, city, openweathermapAccessKey);
            return WeatherResponse.builder()
                    .temperatureDegrees(Common.doubleToInteger(apiResult.getMain().getTemp()))
                    .windSpeed(Common.doubleToInteger(apiResult.getWind().getSpeed())).build();
        } catch (Exception e) {
            log.error("error when getting weather from openweathermap", e);
            return null;
        }
    }
}
