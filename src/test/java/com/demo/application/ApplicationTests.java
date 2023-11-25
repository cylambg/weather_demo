package com.demo.application;

import com.demo.application.dto.OpenweathermapResponse;
import com.demo.application.dto.WeatherstackResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.application.dto.WeatherResponse;
import com.demo.application.service.WeatherService;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ApplicationTests {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private WeatherService weatherService;

	ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		weatherService = new WeatherService(restTemplate);
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getWeatherFromWeatherstackTest() throws Exception {
		String apiResponseStr = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("WeatherstackResponse.json").toURI())));;
		WeatherstackResponse apiResponse = objectMapper.readValue(apiResponseStr, WeatherstackResponse.class);
		Mockito.when(restTemplate.getForObject(any(), eq(WeatherstackResponse.class), eq("london"), any())).thenReturn(apiResponse);
		WeatherResponse expected = new WeatherResponse();
		expected.setTemperatureDegrees(3);
		expected.setWindSpeed(11);
		WeatherResponse actual = weatherService.getWeather("london");
		Assertions.assertEquals(expected, actual);
	}

	@Test
	void getWeatherFromOpenweathermapTest() throws Exception {
		String apiResponseStr = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("OpenweathermapResponse.json").toURI())));;
		OpenweathermapResponse apiResponse = objectMapper.readValue(apiResponseStr, OpenweathermapResponse.class);
		Mockito.when(restTemplate.getForObject(any(), eq(OpenweathermapResponse.class), eq("london"), any())).thenReturn(apiResponse);
		WeatherResponse expected = new WeatherResponse();
		expected.setTemperatureDegrees(3);
		expected.setWindSpeed(0);
		WeatherResponse actual = weatherService.getWeather("london");
		Assertions.assertEquals(expected, actual);
	}
}
