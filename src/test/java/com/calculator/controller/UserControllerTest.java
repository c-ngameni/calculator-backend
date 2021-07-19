package com.calculator.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.calculator.dto.ResponseDto;
import com.calculator.dto.UserDto;
import com.calculator.entity.User;
import com.calculator.exception.CalculatorException;
import com.calculator.service.UserService;
import com.calculator.utils.MessageConstants;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@MockBean
	private UserService userService;

	@MockBean
	private ModelMapper modelMapper;

	private UriComponentsBuilder uriComponentsBuilder;

	private UserDto userDto;

	private HttpEntity<UserDto> request;

	@BeforeEach
	public void init() {

		uriComponentsBuilder = UriComponentsBuilder.newInstance().scheme("http").port(port).host("localhost");

		userDto = new UserDto();
		userDto.setFirstName("John");
		userDto.setLastName("DOE");
		userDto.setEmail("john.doe@gmail.com");
		userDto.setPassword("P@ssw0rd");

		request = new HttpEntity(userDto);
	}

	@Test
	void testRegisterWhenUserAlreadyRegistered() throws CalculatorException {

		User user = new User();
		when(modelMapper.map(userDto, User.class)).thenReturn(user);
		when(userService.register(user)).thenThrow(CalculatorException.class);

		URI url = uriComponentsBuilder.path("/rest/users/registration").build().encode().toUri();
		ResponseEntity<ResponseDto> response = restTemplate.postForEntity(url, request, ResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(MessageConstants.UNSUCCESSFUL_OPERATION, response.getBody().getMessage());
	}

	@Test
	void testRegisterWhenUserNotAlreadyRegistered() throws CalculatorException {

		User user = new User();
		when(modelMapper.map(userDto, User.class)).thenReturn(user);
		when(userService.register(user)).thenReturn(null);

		URI url = uriComponentsBuilder.path("/rest/users/registration").build().encode().toUri();
		ResponseEntity<ResponseDto> response = restTemplate.postForEntity(url, request, ResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(MessageConstants.ERROR_OCCURRED, response.getBody().getMessage());
	}

	@Test
	void testRegister() throws CalculatorException {

		User user = new User();
		when(modelMapper.map(userDto, User.class)).thenReturn(user);
		when(userService.register(user)).thenReturn(user);

		URI url = uriComponentsBuilder.path("/rest/users/registration").build().encode().toUri();
		ResponseEntity<ResponseDto> response = restTemplate.postForEntity(url, request, ResponseDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertEquals(MessageConstants.SUCCESSFUL_OPERATION, response.getBody().getMessage());
	}

	@Test
	void testGetAllUsers() throws Exception {

		URI url = uriComponentsBuilder.path("/rest/users").build().encode().toUri();
		ResponseEntity<UserDtoList> response = restTemplate.getForEntity(url, UserDtoList.class);

		assertNotNull(response);
		assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(CollectionUtils.isEmpty(response.getBody().getUserDtos()));
	}

}
