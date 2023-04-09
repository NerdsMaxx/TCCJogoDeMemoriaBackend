package com.tcc.app.web.memory_game.api.configurations;

import com.tcc.app.web.memory_game.api.repositories.UserRepository;
import com.tcc.app.web.memory_game.api.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
    
    private final TokenService tokenService;
    private final UserRepository userRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = getToken(request);
        
        if (jwtToken != null) {
            String subject = tokenService.getSubject(jwtToken);
            UserDetails userDetails = userRepository.findByUsernameOrEmail(subject);
            final var authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                                                                               userDetails.getAuthorities());
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
    
    private String getToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        return (authorizationHeader != null) ?
               authorizationHeader.replace("Bearer ", "") :
               null;
    }
}