package com.fluxit.test.springboot.service;

import com.fluxit.test.springboot.dto.AuthInfoDto;
import com.fluxit.test.springboot.dto.AuthResponseDto;
import com.fluxit.test.springboot.dto.UserDto;

public interface IUserService {

	/**
	 * Retorna informacion sobre la autenticacion a partir del token jwt, que viene
	 * en el encabezado Authorized. Si la autenticacion es {@link AuthInfoDto#isAuthenticated() valida} 
	 * me retorna informacion del usuario  
	 * @param authorizationBearerHeader
	 * @return
	 */
	public AuthInfoDto getAuthenticatedInfo(String authorizationBearerHeader);

	/**
	 * Verifica si el usuario y contraseña de userDto es correcto, y retorna
	 * junto con el estado de la respuesta del servicio un token jwt para poder
	 * acceder al resto de los servicios de la api
	 * @param userDto
	 * @return
	 */
	public AuthResponseDto authenticate(final UserDto userDto);
}
