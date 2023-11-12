package com.demo.application.controller;

import com.demo.application.dto.WeatherResponse;
import com.demo.application.service.WeatherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( value = "/v1" )
@Slf4j
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @RequestMapping( value = "/weather",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<WeatherResponse> getWeather(
            @RequestParam String city
    ) {
        WeatherResponse response = weatherService.getWeather(city);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
