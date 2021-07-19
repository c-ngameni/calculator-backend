package com.calculator.service;

import java.util.Optional;

import com.calculator.entity.PasswordResetToken;
import com.calculator.entity.User;

public interface PasswordResetTokenService {

	PasswordResetToken createPasswordResetTokenForUser(User user, String token);

	String validatePasswordResetToken(String token);

	Optional<User> getUserByPasswordResetToken(String token);

	void deleteAll();

}
