package com.fluxit.test.springboot.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fluxit.test.springboot.util.IConstants;

public class UserDto {

	@NotNull(message = IConstants.ERROR_INVALID_USER_NAME)
	@Size(min = IConstants.USER_NAME_MIN_LENGTH, max = IConstants.USER_NAME_MAX_LENGTH, message = IConstants.ERROR_INVALID_USER_NAME)
	private String userName;
	
	@NotNull(message = IConstants.ERROR_INVALID_PASSWORD)
	@Size(min = IConstants.PASS_MIN_LENGTH, max = IConstants.PASS_MAX_LENGTH, message = IConstants.ERROR_INVALID_PASSWORD)
	private String pass;

	public UserDto() {}
	
	public UserDto(String userName, String pass) {
		this.userName = userName;
		this.pass = pass;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
}
