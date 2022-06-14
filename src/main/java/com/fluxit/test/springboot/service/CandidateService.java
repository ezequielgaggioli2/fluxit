package com.fluxit.test.springboot.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fluxit.test.springboot.dto.CandidateDto;
import com.fluxit.test.springboot.dto.CandidateResponseDto;
import com.fluxit.test.springboot.dto.SearchCandidateResponseDto;
import com.fluxit.test.springboot.dto.SearchResponseDto;
import com.fluxit.test.springboot.entity.Candidate;
import com.fluxit.test.springboot.entity.User;
import com.fluxit.test.springboot.repository.ICandidateRepository;
import com.fluxit.test.springboot.util.ResponseCodeEnum;

@Service
public class CandidateService implements ICandidateService {

	private ICandidateRepository candidateRep;
	
	private ModelMapper modelMapper;
	
	@Autowired
	public CandidateService(ICandidateRepository candidateRep, ModelMapper modelMapper) {
		
		this.candidateRep = candidateRep;
		this.modelMapper = modelMapper;
	}
	
	@Override
	@Transactional
	public CandidateResponseDto addCandidate(final CandidateDto candidateDto, final User authUser) {
		
		if(candidateDto == null) {
			
			return null;
		}
		
		boolean candidateExist = candidateRep.findById(candidateDto.getDni()).isPresent();
		
		CandidateResponseDto response = new CandidateResponseDto();
		
		if(candidateExist) {
			
			response.setCandidate(candidateDto);
			
			response.setError(ResponseCodeEnum.CANDIDATE_EXIST);
			
			return response;
		}
		
		Date actualDate = new Date();
		
		Candidate newCandidate = modelMapper.map(candidateDto, Candidate.class);
		
		newCandidate.setCreatedBy(authUser);
		
		newCandidate.setCreatedDate(actualDate);
		
		newCandidate.setUpdatedBy(authUser);
		
		newCandidate.setUpdatedDate(actualDate);
		
		Candidate newCandidateSaved = candidateRep.save(newCandidate);
		
		CandidateDto candidateSavedDto = modelMapper.map(newCandidateSaved, CandidateDto.class);
		
		response.setCandidate(candidateSavedDto);
		
		response.setError(ResponseCodeEnum.OK);

		return response;
	}
	
	@Override
	@Transactional
	public CandidateResponseDto editCandidate(final CandidateDto candidateDto, final int dni, final User authUser) {
		
		if(candidateDto == null) {
			
			return null;
		}
		
		CandidateResponseDto response = new CandidateResponseDto();
		
		response.setError(ResponseCodeEnum.CANDIDATE_NOT_EXIST);
		
		Optional<Candidate> candidateToEditOp = candidateRep.findById(dni);
		
		if(candidateToEditOp.isPresent()) {
			
			Date actualDate = new Date();
			
			Candidate candidateToEdit = candidateToEditOp.get();
			
			candidateToEdit.setAddress(candidateDto.getAddress());
			
			candidateToEdit.setDateBirth(candidateDto.getDateBirth());
			
			candidateToEdit.setEmail(candidateDto.getEmail());
			
			candidateToEdit.setName(candidateDto.getName());
			
			candidateToEdit.setSurName(candidateDto.getSurName());
			
			candidateToEdit.setPhoneNumber(candidateDto.getPhoneNumber());
			
			candidateToEdit.setUpdatedBy(authUser);
			
			candidateToEdit.setUpdatedDate(actualDate);
			
			CandidateDto candidateEditedDto = modelMapper.map(candidateToEdit, CandidateDto.class);
			
			response.setCandidate(candidateEditedDto);

			response.setError(ResponseCodeEnum.OK);
		}
		
		return response;
	}
	
	@Override
	@Transactional
	public CandidateResponseDto deleteCandidate(final int dni, final User authUser) {
	
		CandidateResponseDto response = new CandidateResponseDto();
		
		response.setError(ResponseCodeEnum.CANDIDATE_NOT_EXIST);
		
		Optional<Candidate> candidateToDeleteOp = candidateRep.findById(dni);
		
		if(candidateToDeleteOp.isPresent()) {
			
			candidateRep.delete(candidateToDeleteOp.get());
			
			response.setError(ResponseCodeEnum.OK);
		}
		
		return response;
	}
	
	@Override
	public CandidateResponseDto getCandidate(final int dni) {
		
		CandidateResponseDto response = new CandidateResponseDto();
		
		response.setError(ResponseCodeEnum.CANDIDATE_NOT_EXIST);
		
		Optional<Candidate> candidate = candidateRep.findById(dni);

		if(candidate.isPresent()) {
			
			response.setCandidate(modelMapper.map(candidate.get(), CandidateDto.class));
			
			response.setError(ResponseCodeEnum.OK);
		}
		
		return response;
	}
	
	@Override
	public SearchResponseDto searchCandidateBy(final int dni, final String name, final String surName, final int page, final int pageSize) {
		
		SearchResponseDto searchResponse = new SearchResponseDto();
		
		if(page < 0) {
			
			searchResponse.setError(ResponseCodeEnum.FILTER_INVALIDE_PAGE_NUMBER);
			
			return searchResponse;
		}
		
		if(pageSize < 1) {
			
			searchResponse.setError(ResponseCodeEnum.FILTER_INVALIDE_PAGE_SIZE);
			
			return searchResponse;
		}
		
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("dni").ascending());

		Page<Candidate> candidatesPage = candidateRep.findByDniOrNameAndSurName(dni,
																				name.isEmpty() ? name : name+"%",
																				surName.isEmpty() ? surName : surName+"%",
																				pageable);
        
        List<SearchCandidateResponseDto> searchCandidatesDto = candidatesPage.getContent()
        																		.stream()
        																		.map((candidate) -> modelMapper.map(candidate, SearchCandidateResponseDto.class))
        																		.collect(Collectors.toList());
        
        searchResponse.setCandidates(searchCandidatesDto);
        searchResponse.setPage(candidatesPage.getNumber());
        searchResponse.setPageSize(candidatesPage.getSize());
        searchResponse.setTotalCandidates(candidatesPage.getTotalElements());
        searchResponse.setTotalPages(candidatesPage.getTotalPages());
        searchResponse.setError(ResponseCodeEnum.OK);
        
        return searchResponse;
	}
}
