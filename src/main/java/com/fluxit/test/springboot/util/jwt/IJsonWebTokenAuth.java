package com.fluxit.test.springboot.util.jwt;

import java.util.Date;
import java.util.function.Function;

import io.jsonwebtoken.Claims;

public interface IJsonWebTokenAuth {

	/**
	 * Retorna el nombre de usuario contenido en el token jwt
	 * @param token
	 * @return
	 */
	public String getUserNameFromToken(String token);
	
	/**
	 * Retorna la fecha de expiracion contenida en el token jwt
	 * @param token
	 * @return
	 */
	public Date getExpirationDateFromToken(String token);
	
	/**
	 * Retorna informacion de un priveligio contenido en el token jwt
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);
	
	/**
	 * Genera un nuevo token jwt a partir del identificador pasado por argumento 
	 * @param subject
	 * @return
	 */
	public String generateToken(String subject);
	
	/**
	 * Verifica si el token jwt es valido para el nombre de usuario
	 * @param token
	 * @param userName
	 * @return
	 */
	public boolean validateToken(String token, String userName);
}
