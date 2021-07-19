package com.calculator.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils {

	private DateUtils() {

		throw new IllegalStateException("Utility class");
	}

	public static final Date convertToDate(LocalDateTime date) {

		return date == null ? null : Timestamp.valueOf(date);
	}

}
