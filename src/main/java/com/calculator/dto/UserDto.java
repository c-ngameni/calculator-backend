package com.calculator.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

import lombok.Data;
import lombok.ToString;

@Data
public class UserDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Null
	private Long id;

	@NotBlank
	private String firstName;

	@NotBlank
	private String lastName;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@ToString.Exclude
	private String password;

}
