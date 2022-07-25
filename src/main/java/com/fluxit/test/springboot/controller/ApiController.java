package com.fluxit.test.springboot.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fluxit.test.springboot.dto.AuthInfoDto;
import com.fluxit.test.springboot.dto.AuthResponseDto;
import com.fluxit.test.springboot.dto.CandidateDto;
import com.fluxit.test.springboot.dto.CandidateResponseDto;
import com.fluxit.test.springboot.dto.SearchResponseDto;
import com.fluxit.test.springboot.dto.UserDto;
import com.fluxit.test.springboot.service.ICandidateService;
import com.fluxit.test.springboot.service.IUserService;
import com.fluxit.test.springboot.util.IConstants;
import com.fluxit.test.springboot.util.ResponseCodeEnum;

@RestController
@RequestMapping("/fluxit/v1")
public class ApiController {
	
	private final IUserService userServ;
	
	private final ICandidateService candidateServ;
	
	@Autowired
	public ApiController(IUserService userServ, ICandidateService candidateServ) {
		this.userServ = userServ;
		this.candidateServ = candidateServ;
	}
	
	@GetMapping("/pass")
	public String generatePass(String pass) {
		//Metodo de prueba para generar contraseñas bcrypt
		return "Pass: " + new BCryptPasswordEncoder().encode(pass) + "\n";
	}
	
	@PostMapping("/auth")
	public ResponseEntity<AuthResponseDto> authenticate(@Valid @RequestBody UserDto user) {
		
		AuthResponseDto authResponse = userServ.authenticate(user);
		
		if(authResponse == null)
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		
		boolean allOk = authResponse.getError() == ResponseCodeEnum.OK.getCode();
		
		return ResponseEntity.status(allOk ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(authResponse);
	}

	@PostMapping("/candidate")
	public ResponseEntity<CandidateResponseDto> addCandidate(@RequestHeader("Authorization") String authReqHeader,
															 @Valid @RequestBody CandidateDto candidate) {
	
		AuthInfoDto authInfo = userServ.getAuthenticatedInfo(authReqHeader);
		
		if(authInfo.isAuthenticated()) {
		
			CandidateResponseDto addResponse = candidateServ.addCandidate(candidate, authInfo.getAuthenticatedUser());
			
			if(addResponse == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			
			boolean allOk = addResponse.getError() == ResponseCodeEnum.OK.getCode();
			
			return ResponseEntity.status(allOk ? HttpStatus.CREATED : HttpStatus.OK).body(addResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
	
	@PutMapping("/candidate/{dni}")
	public ResponseEntity<CandidateResponseDto> editCandidate(@RequestHeader("Authorization") String authReqHeader,
															  @Valid @RequestBody CandidateDto candidate,
															  @PathVariable(value = "dni") int dni) {
	
		AuthInfoDto authInfo = userServ.getAuthenticatedInfo(authReqHeader);
		
		if(authInfo.isAuthenticated()) {
		
			CandidateResponseDto addResponse = candidateServ.editCandidate(candidate, dni, authInfo.getAuthenticatedUser());
			
			if(addResponse == null)
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
			
			boolean allOk = addResponse.getError() == ResponseCodeEnum.OK.getCode();
			
			return ResponseEntity.status(allOk ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(addResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
	
	@DeleteMapping("/candidate/{dni}")
	public ResponseEntity<CandidateResponseDto> deleteCandidate(@RequestHeader("Authorization") String authReqHeader,
																@PathVariable(value = "dni") int dni) {
	
		AuthInfoDto authInfo = userServ.getAuthenticatedInfo(authReqHeader);
		
		if(authInfo.isAuthenticated()) {
		
			CandidateResponseDto deleteResponse = candidateServ.deleteCandidate(dni, authInfo.getAuthenticatedUser());
			
			boolean allOk = deleteResponse.getError() == ResponseCodeEnum.OK.getCode();
			
			return ResponseEntity.status(allOk ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(deleteResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
	
	@GetMapping("/candidate")
	public ResponseEntity<SearchResponseDto> searchCandidateBy(@RequestHeader("Authorization") String authReqHeader,
															   @RequestParam(value = "dni", defaultValue = "0", required = false) int dni,
														 	   @RequestParam(value = "name", defaultValue = "", required = false) String name,
														 	   @RequestParam(value = "surName", defaultValue = "", required = false) String surName,
														 	   @RequestParam(value = "page", defaultValue = IConstants.DEFAULT_PAGE, required = false) int page,
														 	   @RequestParam(value = "pageSize", defaultValue = IConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize) {
	
		AuthInfoDto authInfo = userServ.getAuthenticatedInfo(authReqHeader);
		
		if(authInfo.isAuthenticated()) {
			
			SearchResponseDto searchResponse = candidateServ.searchCandidateBy(dni, name, surName, page, pageSize);
			
			boolean allOk = searchResponse.getError() == ResponseCodeEnum.OK.getCode();
			
			return ResponseEntity.status(allOk ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(searchResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
	
	@GetMapping("/candidate/{dni}")
	public ResponseEntity<CandidateResponseDto> getCandidate(@RequestHeader("Authorization") String authReqHeader,
										  					 @PathVariable(value = "dni") int dni) {
		
		AuthInfoDto authInfo = userServ.getAuthenticatedInfo(authReqHeader);
		
		if(authInfo.isAuthenticated()) {
			
			CandidateResponseDto getResponse = candidateServ.getCandidate(dni);
			
			boolean allOk = getResponse.getError() == ResponseCodeEnum.OK.getCode();
			
			return ResponseEntity.status(allOk ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(getResponse);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
}
