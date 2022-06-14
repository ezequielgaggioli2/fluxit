package com.fluxit.test.springboot.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fluxit.test.springboot.util.IConstants;

@Entity
@Table(name = "tbl_candidate")
public class Candidate {

	@Id
	@Column(nullable = false, unique = true)
	private int dni;
	
	@Column(nullable = false, length = IConstants.CANDIDATE_NAME_MAX_LENGTH)
	private String name;
	
	@Column(nullable = false, length = IConstants.CANDIDATE_SURNAME_MAX_LENGTH)
	private String surName;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "date_birth", nullable = false)
	private Date dateBirth;
	
	@Column(nullable = false, length = IConstants.EMAIL_MAX_LENGTH)
	private String email;
	
	@Column(nullable = false, length = IConstants.CANDIDATE_ADDRESS_MAX_LENGTH)
	private String address;
	
	@Column(nullable = false, length = IConstants.CANDIDATE_PHONE_MAX_LENGTH)
	private String phoneNumber;
	
	@ManyToOne(optional = false)
	private User createdBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, updatable = false)
	private Date createdDate;
	
	@ManyToOne(optional = false)
	private User updatedBy;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_date", nullable = false)
	private Date updatedDate;

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

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}
}
