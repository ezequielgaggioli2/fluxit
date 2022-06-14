package com.fluxit.test.springboot.dto;

public class CandidateResponseDto extends ResponseDto {

	private CandidateDto candidate;
	
	public CandidateDto getCandidate() {
		return candidate;
	}

	public void setCandidate(CandidateDto candidate) {
		this.candidate = candidate;
	}
}