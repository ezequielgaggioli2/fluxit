package com.fluxit.test.springboot.service;

import com.fluxit.test.springboot.dto.CandidateDto;
import com.fluxit.test.springboot.dto.CandidateResponseDto;
import com.fluxit.test.springboot.dto.SearchResponseDto;
import com.fluxit.test.springboot.entity.User;

public interface ICandidateService {

	public CandidateResponseDto addCandidate(final CandidateDto candidateDto, final User authUser);
	
	public CandidateResponseDto editCandidate(final CandidateDto candidateDto, final int dni, final User authUser);
	
	public CandidateResponseDto deleteCandidate(final int dni, final User authUser);
	
	public CandidateResponseDto getCandidate(final int dni);
	
	public SearchResponseDto searchCandidateBy(final int dni, final String name, final String surName, final int page, final int pageSize);
}