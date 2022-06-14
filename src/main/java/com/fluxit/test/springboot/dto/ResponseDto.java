package com.fluxit.test.springboot.dto;

import com.fluxit.test.springboot.util.ResponseCodeEnum;

public class ResponseDto {

	private int error;
	
	private String message;
	
	public void setError(ResponseCodeEnum resp) {
		
		setError(resp.getCode());
		setMessage(resp.getMessage());
	}

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
