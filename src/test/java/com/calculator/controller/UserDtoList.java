package com.calculator.controller;

import java.util.ArrayList;
import java.util.List;

import com.calculator.dto.UserDto;

public class UserDtoList {

	private List<UserDto> userDtos;

	public UserDtoList() {

		userDtos = new ArrayList<>();
	}

	public List<UserDto> getUserDtos() {

		return userDtos;
	}

	public void setUserDtos(List<UserDto> userDtos) {

		this.userDtos = userDtos;
	}

	@Override
	public String toString() {

		return "UserDtoList [userDtos=" + userDtos + "]";
	}

}
