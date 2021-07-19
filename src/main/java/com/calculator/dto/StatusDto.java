package com.calculator.dto;

import java.io.Serializable;
import java.util.Date;

import com.calculator.utils.CustomDateDeserializer;
import com.calculator.utils.CustomDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;

@Data
public class StatusDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String status;

	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonDeserialize(using = CustomDateDeserializer.class)
	private Date uptime;

}
