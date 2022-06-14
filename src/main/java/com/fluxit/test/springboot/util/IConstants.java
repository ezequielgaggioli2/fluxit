package com.fluxit.test.springboot.util;

public interface IConstants {
	
	public final String DEFAULT_PAGE = "0";
	
	public final String DEFAULT_PAGE_SIZE = "20";

	public final int USER_NAME_MIN_LENGTH = 3;
	
	public final int USER_NAME_MAX_LENGTH = 50;
	
	public final int CANDIDATE_NAME_MAX_LENGTH = 50;
	
	public final int CANDIDATE_PHONE_MAX_LENGTH = 40;
	
	public final int CANDIDATE_SURNAME_MAX_LENGTH = 30;
	
	public final int CANDIDATE_ADDRESS_MAX_LENGTH = 100;
	
	public final int EMAIL_MAX_LENGTH = 320;
	
	public final int PASS_MIN_LENGTH = 4;
	
	public final int PASS_MAX_LENGTH = 50;
	
	public final int PASS_BCRYPT_MAX_LENGTH = 72;
	
	public final String ERROR_INVALID_USER_NAME = "El nombre de usuario (userName) debe tener entre "+IConstants.USER_NAME_MIN_LENGTH+" y "+IConstants.USER_NAME_MAX_LENGTH+" caracteres";
	
	public final String ERROR_INVALID_PASSWORD = "La contraseña (pass) debe tener entre "+IConstants.PASS_MIN_LENGTH+" y "+IConstants.PASS_MAX_LENGTH+" caracteres";
	
	public final String ERROR_INVALID_CANDIDATE_DNI = "El dni (dni) no puede ser nulo o vacío";
	
	public final String ERROR_INVALID_CANDIDATE_NAME = "Ingrese un nombre (name) válido. Máxima longitud "+IConstants.CANDIDATE_NAME_MAX_LENGTH+" caracteres";
	
	public final String ERROR_INVALID_CANDIDATE_SURNAME = "Ingrese un apellido (surName) válido. Máxima longitud "+IConstants.CANDIDATE_SURNAME_MAX_LENGTH+" caracteres";
	
	public final String ERROR_INVALID_CANDIDATE_DATEBIRTH = "Ingrese una fecha de nacimiento (dateBirth)";
	
	public final String ERROR_INVALID_CANDIDATE_EMAIL = "Ingrese un e-mail (email) válido. Máxima longitud "+IConstants.EMAIL_MAX_LENGTH+" caracteres";
	
	public final String ERROR_INVALID_CANDIDATE_ADDRESS = "Ingrese una dirección (address) válida. Máxima longitud "+IConstants.CANDIDATE_ADDRESS_MAX_LENGTH+" caracteres";
	
	public final String ERROR_INVALID_CANDIDATE_PHONE = "Ingrese un teléfono (phoneNumber) válido. Máxima longitud "+IConstants.CANDIDATE_PHONE_MAX_LENGTH+" caracteres";
}
