package com.calculator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import com.calculator.dto.GreetingDto;
import com.calculator.dto.StatusDto;
import com.calculator.utils.DateUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GreetingControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private UriComponentsBuilder uriComponentsBuilder;

	private ObjectMapper objectMapper;

	@BeforeEach
	public void init() {

		uriComponentsBuilder = UriComponentsBuilder.newInstance().scheme("http").port(port).host("localhost");
		objectMapper = new ObjectMapper();
	}

	@Test
	void testGreet() throws Exception {

		URI url = uriComponentsBuilder.path("/rest/greeting").build().encode().toUri();
		ResponseEntity<GreetingDto> response = restTemplate.getForEntity(url, GreetingDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("Hello World!", response.getBody().getContent());
	}

	@Test
	void testGetApplicationStatus() {

		URI url = uriComponentsBuilder.path("/rest/status").build().encode().toUri();
		ResponseEntity<StatusDto> response = restTemplate.getForEntity(url, StatusDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals("UP", response.getBody().getStatus());
		assertNotNull(response.getBody().getUptime());
	}

	@Test
	void testSerializeStatusDto() throws JsonProcessingException {

		StatusDto statusDto = new StatusDto();
		statusDto.setStatus("UP");
		statusDto.setUptime(DateUtils.convertToDate(LocalDateTime.of(2021, Month.JULY, 1, 12, 00)));

		String result = objectMapper.writeValueAsString(statusDto);

		assertTrue(result.contains("1/07/2021 12:00"));
	}

	@Test
	void testDeseralizeStatusDto() throws JsonMappingException, JsonProcessingException {

		String status = "{ \"status\": \"UP\", \"uptime\": \"1/07/2021 12:00\"}";

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		objectMapper.setDateFormat(formatter);

		StatusDto statusDto = objectMapper.readerFor(StatusDto.class).readValue(status);

		assertEquals("01/07/2021 12:00", formatter.format(statusDto.getUptime()));
	}

}
