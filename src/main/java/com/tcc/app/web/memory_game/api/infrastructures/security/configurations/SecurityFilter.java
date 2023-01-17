package com.tcc.app.web.memory_game.api.infrastructures.security.configurations;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tcc.app.web.memory_game.api.infrastructures.security.repositories.UserRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.TokenService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserRepository userRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {
		var jwtToken = getToken(request);

		if (jwtToken != null) {
			var subject = tokenService.getSubject(jwtToken);
			var userDetails = userRepository.findByUsername( subject );
			var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
					userDetails.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}

	private String getToken(HttpServletRequest request) {
		var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

		if (authorizationHeader != null) {
			return authorizationHeader.replace("Bearer ", "");
		}

		return null;
	}
}