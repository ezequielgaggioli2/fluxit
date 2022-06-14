package com.fluxit.test.springboot.dto;

import java.util.List;

public class SearchResponseDto extends ResponseDto {

	private List<SearchCandidateResponseDto> candidates;
	
	private int page;
	
    private int pageSize;
    
    private long totalCandidates;
    
    private int totalPages;

	public List<SearchCandidateResponseDto> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<SearchCandidateResponseDto> candidates) {
		this.candidates = candidates;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalCandidates() {
		return totalCandidates;
	}

	public void setTotalCandidates(long totalCandidates) {
		this.totalCandidates = totalCandidates;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
