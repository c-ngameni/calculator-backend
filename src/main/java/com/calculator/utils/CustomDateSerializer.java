package com.calculator.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class CustomDateSerializer extends StdSerializer<Date> {

	private static final long serialVersionUID = 1L;

	private final SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	public CustomDateSerializer() {

		this(null);
	}

	public CustomDateSerializer(Class clazz) {

		super(clazz);
	}

	@Override
	public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider arg2) throws IOException {

		jsonGenerator.writeString(formatter.format(value));
	}

}
