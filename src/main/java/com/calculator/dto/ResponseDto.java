package com.calculator.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ResponseDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String message;

}
