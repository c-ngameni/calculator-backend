package com.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.calculator.entity.PasswordResetToken;
import com.calculator.entity.User;
import com.calculator.repository.PasswordResetTokenRepository;
import com.calculator.utils.DateUtils;

@ExtendWith(MockitoExtension.class)
class PasswordResetTokenServiceImplTest {

	@InjectMocks
	private PasswordResetTokenServiceImpl passwordResetTokenServiceImpl;

	@Mock
	private PasswordResetTokenRepository passwordResetTokenRepository;

	private String token;

	@BeforeEach
	public void init() {

		token = "12345";
	}

	@Test
	void testCreatePasswordResetTokenForUser() {

		PasswordResetToken passwordResetToken = new PasswordResetToken(new User(), token);
		when(passwordResetTokenRepository.save(passwordResetToken)).thenReturn(passwordResetToken);

		PasswordResetToken result = passwordResetTokenServiceImpl.createPasswordResetTokenForUser(new User(), token);

		assertEquals(passwordResetToken, result);
		verify(passwordResetTokenRepository, times(1)).save(passwordResetToken);
	}

	@Nested
	class ValidatePasswordResetToken {

		@AfterEach
		public void check() {

			verify(passwordResetTokenRepository, times(1)).findByToken(token);
		}

		@Test
		void testValidatePasswordResetTokenWhenPasswordResetTokenNotExists() {

			when(passwordResetTokenRepository.findByToken(token)).thenReturn(null);

			String result = passwordResetTokenServiceImpl.validatePasswordResetToken(token);

			assertEquals("Invalid token", result);
		}

		@Test
		void testValidatePasswordResetTokenWhenPasswordResetTokenHasExpired() {

			PasswordResetToken passwordResetToken = new PasswordResetToken();
			passwordResetToken.setId(1L);
			passwordResetToken.setUser(new User());
			passwordResetToken.setToken(token);
			passwordResetToken.setExpiryDate(DateUtils.convertToDate(LocalDateTime.of(2021, Month.JULY, 1, 0, 0)));
			when(passwordResetTokenRepository.findByToken(token)).thenReturn(passwordResetToken);

			String result = passwordResetTokenServiceImpl.validatePasswordResetToken(token);

			assertEquals("Expired token", result);
		}

		@Test
		void testValidatePasswordResetTokenWhenPasswordResetTokenHasNotExpired() {

			PasswordResetToken passwordResetToken = new PasswordResetToken();
			passwordResetToken.initExpiryDate();
			when(passwordResetTokenRepository.findByToken(token)).thenReturn(passwordResetToken);

			String result = passwordResetTokenServiceImpl.validatePasswordResetToken(token);

			assertNull(result);
		}

	}

	@Test
	void testGetUserByPasswordResetTokenWhenPasswordResetTokenNotExists() {

		when(passwordResetTokenRepository.findByToken(token)).thenReturn(null);

		Optional<User> user = passwordResetTokenServiceImpl.getUserByPasswordResetToken(token);

		assertFalse(user.isPresent());
		verify(passwordResetTokenRepository, times(1)).findByToken(token);
	}

	@Test
	void testGetUserByPasswordResetTokenWhenPasswordResetTokenExists() {

		when(passwordResetTokenRepository.findByToken(token)).thenReturn(new PasswordResetToken(new User(), token));

		Optional<User> user = passwordResetTokenServiceImpl.getUserByPasswordResetToken(token);

		assertTrue(user.isPresent());
		assertEquals(new User(), user.get());
		verify(passwordResetTokenRepository, times(1)).findByToken(token);
	}

}
