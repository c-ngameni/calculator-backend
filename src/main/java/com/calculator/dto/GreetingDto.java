package com.calculator.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class GreetingDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String content;

}
