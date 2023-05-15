package com.tcc.app.web.memory_game.api.services;

import com.tcc.app.web.memory_game.api.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class AuthenticationService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return Optional.ofNullable(userRepository.findByUsernameOrEmail(usernameOrEmail))
                       .orElseThrow(() -> new UsernameNotFoundException("ignored"));
    }
    
}