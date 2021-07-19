package com.calculator.service;

import java.util.List;
import java.util.Optional;

import com.calculator.entity.User;
import com.calculator.exception.CalculatorException;

public interface UserService {

	User register(User user) throws CalculatorException;

	Optional<User> findByEmail(String email);

	List<User> findAll();

	boolean updatePassword(User user, String password);

}
