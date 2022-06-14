package com.fluxit.test.springboot.util;

public enum ResponseCodeEnum {

	OK(0, null),
	
	USER_NOT_FOUND(1, "No se encuentra usuario"),
	
	WRONG_PASSWORD(2, "Contraseña incorrecta"),
	
	CANDIDATE_EXIST(3, "Ya existe candidato con el dni ingresado"),
	
	CANDIDATE_NOT_EXIST(4, "No existe candidato con el dni ingresado"),
	
	FILTER_INVALIDE_PAGE_NUMBER(5, "El numero de pagina (page) debe ser mayor o igual a cero"),
	
	FILTER_INVALIDE_PAGE_SIZE(6, "El tamaño de la pagina (pageSize) debe ser mayor a cero");
	
	private final int code;
	
	private final String message;

	private ResponseCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
