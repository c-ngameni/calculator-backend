package com.calculator.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.CollectionUtils;

import com.calculator.entity.PasswordResetToken;
import com.calculator.entity.User;
import com.calculator.exception.CalculatorException;
import com.calculator.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	private static final String ENCODED_PASSWORD = "${Abcd€fgk1J}";

	private User user;

	@BeforeEach
	public void init() {

		user = new User();
		user.setId(1L);
		user.setFirstName("John");
		user.setLastName("DOE");
		user.setEmail("john.doe@gmail.com");
		user.setPassword("P@ssw0rd");
		user.setPasswordResetTokens(Arrays.asList(new PasswordResetToken()));
	}

	@Test
	void testRegisterWhenUserIsRegistered() {

		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

		assertThrows(CalculatorException.class, () -> userServiceImpl.register(user));
	}

	@Test
	void testRegister() {

		when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
		when(passwordEncoder.encode(user.getPassword())).thenReturn(ENCODED_PASSWORD);
		when(userRepository.save(user)).thenReturn(user);

		User result = null;
		try {
			result = userServiceImpl.register(user);
		} catch (CalculatorException e) {
		}

		verify(passwordEncoder, times(1)).encode("P@ssw0rd");
		assertEquals(ENCODED_PASSWORD, user.getPassword());
		assertFalse(CollectionUtils.isEmpty(user.getRoles()));
		assertEquals(1, user.getRoles().size());
		assertTrue(user.getRoles().iterator().hasNext());
		assertEquals("USER", user.getRoles().iterator().next().getName());
		assertEquals(user, result);
		verify(userRepository, times(1)).save(user);
	}

	@Test
	void testFindByEmail() {

		when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

		Optional<User> result = userServiceImpl.findByEmail(user.getEmail());

		assertTrue(result.isPresent());
		assertEquals(user, result.get());
	}

	@Test
	void testFindAll() {

		when(userRepository.findAll()).thenReturn(Arrays.asList(user));

		List<User> users = userServiceImpl.findAll();

		assertFalse(CollectionUtils.isEmpty(users));
		assertEquals(1, users.size());
		assertEquals(user, users.get(0));
	}

	@Test
	void testUpdatePassword() {

		when(passwordEncoder.encode("Password")).thenReturn(ENCODED_PASSWORD);
		when(userRepository.save(user)).thenReturn(user);

		boolean isUpdated = userServiceImpl.updatePassword(user, "Password");

		assertTrue(isUpdated);
		assertEquals(ENCODED_PASSWORD, user.getPassword());
		verify(userRepository, times(1)).save(user);
	}

}
