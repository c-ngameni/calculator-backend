package com.calculator.service;

import com.calculator.exception.CalculatorException;

public interface EmailService {

	void sendEmail(String recipient, String subject, String message) throws CalculatorException;

}
