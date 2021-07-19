package com.calculator.utils;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class DateUtilsTest {

	@Test
	void testConvertToDateWhenDateIsNull() {

		assertNull(DateUtils.convertToDate(null));
	}

}
