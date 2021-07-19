package com.calculator.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.dto.ResponseDto;
import com.calculator.service.PasswordResetTokenService;
import com.calculator.utils.MessageConstants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/password-reset-tokens")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class PasswordResetTokenController {

	@Autowired
	private PasswordResetTokenService passwordResetTokenService;

	// @PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping
	public ResponseEntity<ResponseDto> deleteAll() {

		log.info("Deleting all tokens for resetting passwords.");
		passwordResetTokenService.deleteAll();

		ResponseDto responseDto = new ResponseDto();
		responseDto.setMessage(MessageConstants.SUCCESSFUL_OPERATION);
		return ResponseEntity.ok(responseDto);
	}

}
