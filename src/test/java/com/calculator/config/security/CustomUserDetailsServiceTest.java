package com.calculator.config.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.CollectionUtils;

import com.calculator.entity.Role;
import com.calculator.entity.RoleEnum;
import com.calculator.entity.User;
import com.calculator.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

	@InjectMocks
	private CustomUserDetailsService customUserDetailsService;

	@Mock
	private UserRepository userRepository;

	private static final String EMAIL = "john.doe@gmail.com";

	private static final String PASSWORD = "P@ssw0rd";

	@Test
	void testLoadUserByUsernameWhenUserNotExists() {

		when(userRepository.findByEmail(EMAIL)).thenReturn(null);

		assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(EMAIL));
	}

	@Test
	void testLoadUserByUsernameWhenUserExistsAndHasNoRole() {

		User user = new User();
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		when(userRepository.findByEmail(EMAIL)).thenReturn(user);

		org.springframework.security.core.userdetails.User result = customUserDetailsService.loadUserByUsername(EMAIL);

		assertNotNull(result);
		assertEquals(EMAIL, result.getUsername());
		assertEquals(PASSWORD, result.getPassword());
		assertTrue(CollectionUtils.isEmpty(result.getAuthorities()));
	}

	@Test
	void testLoadUserByUsernameWhenUserExistsAndHasAtLeastOneRole() {

		User user = new User();
		user.setEmail(EMAIL);
		user.setPassword(PASSWORD);
		user.setRoles(Collections.singleton(new Role(RoleEnum.USER.toString())));
		when(userRepository.findByEmail(EMAIL)).thenReturn(user);

		org.springframework.security.core.userdetails.User result = customUserDetailsService.loadUserByUsername(EMAIL);

		assertNotNull(result);
		assertEquals(EMAIL, result.getUsername());
		assertEquals(PASSWORD, result.getPassword());
		assertFalse(CollectionUtils.isEmpty(result.getAuthorities()));
		assertEquals(1, result.getAuthorities().size());
	}

}
