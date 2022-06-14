package com.fluxit.test.springboot.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fluxit.test.springboot.util.IConstants;

public class CandidateDto {

	private int dni;
	
	@NotEmpty(message = IConstants.ERROR_INVALID_CANDIDATE_NAME)
	@Size(max = IConstants.CANDIDATE_NAME_MAX_LENGTH, message = IConstants.ERROR_INVALID_CANDIDATE_NAME)
	private String name;
	
	@NotEmpty(message = IConstants.ERROR_INVALID_CANDIDATE_SURNAME)
	@Size(max = IConstants.CANDIDATE_SURNAME_MAX_LENGTH, message = IConstants.ERROR_INVALID_CANDIDATE_SURNAME)
	private String surName;
	
	@NotNull(message = IConstants.ERROR_INVALID_CANDIDATE_DATEBIRTH)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateBirth;
	
	@NotEmpty(message = IConstants.ERROR_INVALID_CANDIDATE_EMAIL)
	@Size(max = IConstants.EMAIL_MAX_LENGTH, message = IConstants.ERROR_INVALID_CANDIDATE_EMAIL)
	@Email(message = IConstants.ERROR_INVALID_CANDIDATE_EMAIL)
	private String email;
	
	@NotEmpty(message = IConstants.ERROR_INVALID_CANDIDATE_ADDRESS)
	@Size(max = IConstants.CANDIDATE_ADDRESS_MAX_LENGTH, message = IConstants.ERROR_INVALID_CANDIDATE_ADDRESS)
	private String address;
	
	@NotEmpty(message = IConstants.ERROR_INVALID_CANDIDATE_PHONE)
	@Size(max = IConstants.CANDIDATE_PHONE_MAX_LENGTH, message = IConstants.ERROR_INVALID_CANDIDATE_PHONE)
	private String phoneNumber;

	public int getDni() {
		return dni;
	}

	public void setDni(int dni) {
		this.dni = dni;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(String surName) {
		this.surName = surName;
	}

	public Date getDateBirth() {
		return dateBirth;
	}

	public void setDateBirth(Date dateBirth) {
		this.dateBirth = dateBirth;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}