package com.calculator.utils;

public class MessageConstants {

	private MessageConstants() {

		throw new IllegalStateException("Utility class");
	}

	public static final String SUCCESSFUL_OPERATION = "Successful operation";

	public static final String UNSUCCESSFUL_OPERATION = "Unsuccessful operation";

	public static final String ERROR_OCCURRED = "An error occurred.";

	public static final String INVALID_OR_EXPIRED_TOKEN = "Your token is invalid or has expired.";

	public static final String PASSWORD_UPDATE_FAILURE = "Unable to update the password.";

}
