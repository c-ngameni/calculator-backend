package com.calculator.utils;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomDateDeserializer extends StdDeserializer<Date> {

	private static final long serialVersionUID = 1L;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public CustomDateDeserializer() {

		this(null);
	}

	public CustomDateDeserializer(Class clazz) {

		super(clazz);
	}

	@Override
	public Date deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {

		try {
			return formatter.parse(jsonParser.getText());
		} catch (ParseException e) {
			log.error("An error occurred: {}", e.getMessage());
		}
		return null;
	}

}
