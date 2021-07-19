package com.calculator.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.calculator.entity.Role;
import com.calculator.entity.RoleEnum;
import com.calculator.entity.User;
import com.calculator.exception.CalculatorException;
import com.calculator.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public User register(User user) throws CalculatorException {

		if (findByEmail(user.getEmail()).isPresent()) {
			throw new CalculatorException();
		}

		// Encoding the password.
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(Collections.singleton(new Role(RoleEnum.USER.toString())));
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findByEmail(String email) {

		return Optional.ofNullable(userRepository.findByEmail(email));
	}

	@Override
	public List<User> findAll() {

		Iterable<User> foundUsers = userRepository.findAll();

		List<User> users = new ArrayList<>();
		Iterator<User> iterator = foundUsers.iterator();
		while (iterator.hasNext()) {
			users.add(iterator.next());
		}

		return users;
	}

	@Override
	public boolean updatePassword(User user, String password) {

		// Encoding the password.
		user.setPassword(passwordEncoder.encode(password));
		return userRepository.save(user) != null;
	}

}
