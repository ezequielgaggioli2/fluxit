package com.fluxit.test.springboot.dto;

public class AuthResponseDto extends ResponseDto {

	private String token;
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
