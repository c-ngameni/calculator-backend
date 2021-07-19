package com.calculator.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.dto.PasswordUpdateDto;
import com.calculator.dto.ResponseDto;
import com.calculator.dto.UserDto;
import com.calculator.entity.User;
import com.calculator.exception.CalculatorException;
import com.calculator.service.EmailService;
import com.calculator.service.PasswordResetTokenService;
import com.calculator.service.UserService;
import com.calculator.utils.MessageConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Api(description = "API pour les opérations sur les utilisateurs.")
@Slf4j
public class UserController {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordResetTokenService passwordResetTokenService;

	@Autowired
	private EmailService emailService;

	@ApiOperation(value = "Enregistre un nouvel utilisateur.")
	@PostMapping("/registration")
	public ResponseEntity<ResponseDto> register(@Valid @RequestBody UserDto user) {

		log.info("Registering a new user.");
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage(MessageConstants.SUCCESSFUL_OPERATION);

		try {
			User registeredUser = userService.register(convertToEntity(user));
			if (registeredUser == null) {
				responseDto.setMessage(MessageConstants.ERROR_OCCURRED);
				return new ResponseEntity<>(responseDto, HttpStatus.OK);
			}
		} catch (CalculatorException e) {
			log.error("An error occured: {}", e.getMessage());
			responseDto.setMessage(MessageConstants.UNSUCCESSFUL_OPERATION);
			return new ResponseEntity<>(responseDto, HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@GetMapping("/login")
	public ResponseEntity<UserDto> login(Principal user) {

		log.info("Logging user.");
		if (user == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		Optional<User> foundUser = userService.findByEmail(user.getName());
		if (!foundUser.isPresent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return ResponseEntity.ok(this.convertToDto(foundUser.get()));
	}

	@ApiOperation(value = "Récupère la liste des utilisateurs.")
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public List<UserDto> getAllUsers() {

		log.info("Finding all users.");
		return userService.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
	}

	@ApiOperation(value = "Réinitialise le mot de passe d'un utilisateur.")
	@PostMapping("/password-reset")
	public ResponseEntity<ResponseDto> resetPassword(HttpServletRequest request, @Valid @NotBlank String email) {

		log.info("Resetting password.");
		Optional<User> user = userService.findByEmail(email);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage(MessageConstants.UNSUCCESSFUL_OPERATION);
		if (!user.isPresent()) {
			return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
		}

		String token = UUID.randomUUID().toString();
		passwordResetTokenService.createPasswordResetTokenForUser(user.get(), token);

		String url = getAppUrl(request);
		String message = constructEmailMessage(getChangePasswordUrl(url, token));

		try {
			emailService.sendEmail(email, "[Calculator] Réinitialisation du mot de passe", message);
			responseDto.setMessage(MessageConstants.SUCCESSFUL_OPERATION);
		} catch (CalculatorException e) {
			log.error("An error occurred: {}", e.getMessage());

			responseDto.setMessage(MessageConstants.ERROR_OCCURRED);
			return ResponseEntity.ok(responseDto);
		}

		return ResponseEntity.ok(responseDto);

	}

	@ApiOperation(value = "Vérifie la validité d'un token de réinitialisation de mot de passe.")
	@GetMapping("/password-change")
	public ResponseEntity<ResponseDto> changePassword(@Valid @NotBlank String token) {

		String message = passwordResetTokenService.validatePasswordResetToken(token);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage(MessageConstants.SUCCESSFUL_OPERATION);

		if (message != null) {
			responseDto.setMessage(MessageConstants.INVALID_OR_EXPIRED_TOKEN);
			return ResponseEntity.ok(responseDto);
		}

		return ResponseEntity.ok(responseDto);
	}

	@ApiOperation(value = "Met à jour un mot de passe.")
	@PostMapping("/password-update")
	public ResponseEntity<ResponseDto> updatePassword(@Valid @RequestBody PasswordUpdateDto passwordUpdateDto) {

		log.info("Updating password.");
		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage(MessageConstants.SUCCESSFUL_OPERATION);
		String message = passwordResetTokenService.validatePasswordResetToken(passwordUpdateDto.getToken());

		if (message != null) {
			responseDto.setMessage(MessageConstants.INVALID_OR_EXPIRED_TOKEN);
			return ResponseEntity.ok(responseDto);
		}

		Optional<User> user = passwordResetTokenService.getUserByPasswordResetToken(passwordUpdateDto.getToken());

		if (user.isPresent()) {
			boolean isPasswordUpdated = userService.updatePassword(user.get(), passwordUpdateDto.getNewPassword());

			if (isPasswordUpdated) {
				return ResponseEntity.ok(responseDto);
			}
		}

		responseDto.setMessage(MessageConstants.PASSWORD_UPDATE_FAILURE);
		return ResponseEntity.ok(responseDto);
	}

	private User convertToEntity(UserDto user) {

		return modelMapper.map(user, User.class);
	}

	private UserDto convertToDto(User user) {

		UserDto userDto = modelMapper.map(user, UserDto.class);
		userDto.setPassword(null);
		return userDto;
	}

	private String constructEmailMessage(String url) {

		StringBuilder stringBuilder = new StringBuilder("Voici le lien de réinitialisation de votre mot de passe : ");
		stringBuilder.append(url);
		stringBuilder.append(". Ce lien est valable 24h.");

		return stringBuilder.toString();
	}

	private String getChangePasswordUrl(String url, String token) {

		StringBuilder stringBuilder = new StringBuilder(url);
		stringBuilder.append("/users/password-change?token=");
		stringBuilder.append(token);

		return stringBuilder.toString();
	}

	private String getAppUrl(HttpServletRequest request) {

		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(request.getScheme());
		stringBuilder.append("://");
		stringBuilder.append(request.getServerName());
		stringBuilder.append(":");
		stringBuilder.append(request.getServerPort());
		stringBuilder.append(request.getContextPath());

		return stringBuilder.toString();
	}

}
