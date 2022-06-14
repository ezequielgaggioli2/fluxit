package com.fluxit.test.springboot.util.jwt;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Clase que permite administrar y generar un token jwt.
 * Para que las pruebas unitarias y de PostMan fueran mas faciles se genera el token para un usuario particular
 * pero el tiempo de creacion y expiracion del token es el mismo para cualquier token 
 * @author ezequiel
 */
@Component
public class JsonWebTokenAuth implements IJsonWebTokenAuth {

	//public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

	private final String secret = "I am Satoshi";

	@Override
	public String getUserNameFromToken(String token) {

		return getClaimFromToken(token, Claims::getSubject);
	}

	@Override
	public Date getExpirationDateFromToken(String token) {

		return getClaimFromToken(token, Claims::getExpiration);
	}

	@Override
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {

		final Claims claims = getAllClaimsFromToken(token);

		return claimsResolver.apply(claims);
	}

	@Override
	public String generateToken(String subject) {

		Map<String, Object> claims = new HashMap<>();

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new GregorianCalendar(2022,05,12).getTime()/*new Date(System.currentTimeMillis())*/)
				.setExpiration(new GregorianCalendar(2100,1,1).getTime()/*new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)*/)
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	@Override
	public boolean validateToken(String token, String userName) {

		final String userNameToken = getUserNameFromToken(token);

		return (userNameToken.equals(userName) && !isTokenExpired(token));
	}
	
	private Claims getAllClaimsFromToken(String token) {

		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {

		final Date expiration = getExpirationDateFromToken(token);

		return expiration.before(new Date());
	}
}
