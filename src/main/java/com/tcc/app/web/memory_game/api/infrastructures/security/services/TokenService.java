package com.tcc.app.web.memory_game.api.infrastructures.security.services;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;

@Service
public class TokenService {

	@Value("${api.security.token.issuer}")
	private String issuer;

	@Value("${api.security.token.secret}")
	private String secret;

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
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}