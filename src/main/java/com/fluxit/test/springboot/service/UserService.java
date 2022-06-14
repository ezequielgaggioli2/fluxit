package com.fluxit.test.springboot.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fluxit.test.springboot.dto.AuthInfoDto;
import com.fluxit.test.springboot.dto.AuthResponseDto;
import com.fluxit.test.springboot.dto.UserDto;
import com.fluxit.test.springboot.entity.User;
import com.fluxit.test.springboot.repository.IUserRepository;
import com.fluxit.test.springboot.util.ResponseCodeEnum;
import com.fluxit.test.springboot.util.jwt.IJsonWebTokenAuth;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;

@Service
public class UserService implements IUserService {

	private IUserRepository userRep;
	
	private IJsonWebTokenAuth jsonWebTokenAuth;
	
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	public UserService(IUserRepository userRep, IJsonWebTokenAuth jsonWebTokenAuth, BCryptPasswordEncoder passwordEncoder) {
		this.userRep = userRep;
		this.jsonWebTokenAuth = jsonWebTokenAuth;
		this.passwordEncoder = passwordEncoder; 
	}
	
	@Override
	public AuthInfoDto getAuthenticatedInfo(String authorizationBearerHeader) {
		
		boolean authenticated = false;
		
		User authenticatedUser = null;
		
		if(authorizationBearerHeader != null && authorizationBearerHeader.startsWith("Bearer ")) {
			
			try {
			
				String jwtToken = authorizationBearerHeader.substring(7);
				
				String jwtTokenUserName = jsonWebTokenAuth.getUserNameFromToken(jwtToken);
				
				Optional<User> userOpt = userRep.findByUserName(jwtTokenUserName);
				
				authenticated = userOpt.isPresent() && jsonWebTokenAuth.validateToken(jwtToken, userOpt.get().getUserName());
				
				authenticatedUser = userOpt.isPresent() ? userOpt.get() : null;
			}
			catch (SignatureException | MalformedJwtException e) {}
		} 
		
		return new AuthInfoDto(authenticated, authenticatedUser);
	}
	
	@Override
	public AuthResponseDto authenticate(final UserDto userDto) {
		
		if(userDto == null) {
			
			return null;
		}
		
		Optional<User> userOpt = userRep.findByUserName(userDto.getUserName());
		
		AuthResponseDto authResp = new AuthResponseDto();
		
		authResp.setError(ResponseCodeEnum.USER_NOT_FOUND);
		
		if(userOpt.isPresent()) {
			
			if(passwordEncoder.matches(userDto.getPass(), userOpt.get().getPass())) {
			
				String jwtToken = jsonWebTokenAuth.generateToken(userOpt.get().getUserName());
		
				authResp.setToken(jwtToken);
				
				authResp.setError(ResponseCodeEnum.OK);
			}
			else {
				
				authResp.setError(ResponseCodeEnum.WRONG_PASSWORD);
			}
		}
		
		return authResp;
	}
}
