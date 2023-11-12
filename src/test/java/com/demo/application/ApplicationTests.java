package com.demo.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.demo.application.dto.WeatherResponse;
import com.demo.application.service.WeatherService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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
		weatherService = new WeatherService();
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getWeatherFromWeatherstackTest() throws JsonProcessingException {
		String apiResponseStr = "{\n" +
				"    \"request\": {\n" +
				"        \"type\": \"City\",\n" +
				"        \"query\": \"Melbourne, Australia\",\n" +
				"        \"language\": \"en\",\n" +
				"        \"unit\": \"m\"\n" +
				"    },\n" +
				"    \"location\": {\n" +
				"        \"name\": \"Melbourne\",\n" +
				"        \"country\": \"Australia\",\n" +
				"        \"region\": \"Victoria\",\n" +
				"        \"lat\": \"-37.817\",\n" +
				"        \"lon\": \"144.967\",\n" +
				"        \"timezone_id\": \"Australia/Melbourne\",\n" +
				"        \"localtime\": \"2021-11-29 04:04\",\n" +
				"        \"localtime_epoch\": 1638158640,\n" +
				"        \"utc_offset\": \"11.0\"\n" +
				"    },\n" +
				"    \"current\": {\n" +
				"        \"observation_time\": \"05:04 PM\",\n" +
				"        \"temperature\": 11,\n" +
				"        \"weather_code\": 113,\n" +
				"        \"weather_icons\": [\n" +
				"            \"https://assets.weatherstack.com/images/wsymbols01_png_64/wsymbol_0008_clear_sky_night.png\"\n" +
				"        ],\n" +
				"        \"weather_descriptions\": [\n" +
				"            \"Clear\"\n" +
				"        ],\n" +
				"        \"wind_speed\": 13,\n" +
				"        \"wind_degree\": 280,\n" +
				"        \"wind_dir\": \"W\",\n" +
				"        \"pressure\": 1018,\n" +
				"        \"precip\": 0,\n" +
				"        \"humidity\": 94,\n" +
				"        \"cloudcover\": 0,\n" +
				"        \"feelslike\": 11,\n" +
				"        \"uv_index\": 1,\n" +
				"        \"visibility\": 10,\n" +
				"        \"is_day\": \"no\"\n" +
				"    }\n" +
				"}";
		Map<String, Object> apiResponse = objectMapper.readValue(apiResponseStr, Map.class);
		Mockito.when(restTemplate.getForObject(any(), eq(Map.class), eq("melbourne"), any()))
				.thenReturn(apiResponse);
		WeatherResponse expected = new WeatherResponse();
		expected.setTemperatureDegrees(11);
		expected.setWindSpeed(13);
		WeatherResponse actual = weatherService.getWeather("melbourne");
		Assert.assertEquals(expected, actual);
	}
}
