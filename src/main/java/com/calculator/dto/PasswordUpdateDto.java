package com.calculator.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.ToString;

@Data
public class PasswordUpdateDto {

	@NotBlank
	private String token;

	@NotBlank
	@ToString.Exclude
	private String oldPassword;

	@NotBlank
	@ToString.Exclude
	private String newPassword;

}
