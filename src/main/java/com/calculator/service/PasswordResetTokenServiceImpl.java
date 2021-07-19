package com.calculator.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.calculator.entity.PasswordResetToken;
import com.calculator.entity.User;
import com.calculator.repository.PasswordResetTokenRepository;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public PasswordResetToken createPasswordResetTokenForUser(User user, String token) {

		return passwordResetTokenRepository.save(new PasswordResetToken(user, token));
	}

	@Override
	public String validatePasswordResetToken(String token) {

		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

		if (passwordResetToken == null) {
			return "Invalid token";
		}
		if (passwordResetToken.getExpiryDate().before(new Date())) {
			return "Expired token";
		}

		return null;
	}

	@Override
	public Optional<User> getUserByPasswordResetToken(String token) {

		PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

		if (passwordResetToken == null) {
			return Optional.empty();
		}

		return Optional.ofNullable(passwordResetToken.getUser());
	}

	@Override
	public void deleteAll() {

		passwordResetTokenRepository.deleteAll();
	}

}
