package com.fluxit.test.springboot.dto;

import com.fluxit.test.springboot.entity.User;

public class AuthInfoDto {

	private final boolean authenticated;
	
	private final User authenticatedUser;
	
	public AuthInfoDto(boolean authenticated, User authenticatedUser) {
		super();
		this.authenticated = authenticated;
		this.authenticatedUser = authenticatedUser;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public User getAuthenticatedUser() {
		return authenticatedUser;
	}
}