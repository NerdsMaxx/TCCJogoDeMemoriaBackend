package com.tcc.app.web.memory_game.api.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.tcc.app.web.memory_game.api.entities.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

@Service
public class TokenService {

	@Value("${api.security.token.issuer}")
	private String issuer;

	@Value("${api.security.token.secret}")
	private String secret;
	
	@Value("${api.security.token.very_long}")
	private Boolean veryLong;

	public String generateToken(UserEntity user) {
		try {
			var algorithm = Algorithm.HMAC256(secret);

			return JWT.create().withIssuer(issuer).withSubject(user.getUsername())
					  .withExpiresAt(_getExpirationDate()).sign(algorithm);

		} catch (JWTCreationException exception) {
			throw new RuntimeException("Erro ao gerar token jwt ou expirado!", exception);
		}
	}

	public String getSubject(String jwtToken) {
		try {
			var algorithm = Algorithm.HMAC256(secret);

			return JWT.require(algorithm).withIssuer(issuer).build().verify(jwtToken).getSubject();

		} catch (JWTVerificationException exception) {
			throw new RuntimeException("Token JWT inválido!");
		}
	}

	private Instant _getExpirationDate() {
		if(veryLong) {
			return LocalDateTime.now().plusYears(1).toInstant(ZoneOffset.of("-03:00"));
		}
		
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}