package com.tcc.app.web.memory_game.api.infrastructures.security.services;

import java.util.HashSet;

import com.tcc.app.web.memory_game.api.infrastructures.security.repositories.UserTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserInsertDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.mappers.UserMapper;
import com.tcc.app.web.memory_game.api.infrastructures.security.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserTypeRepository userTypeRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Transactional
    public UserEntity registerNewUser( UserInsertDto userInsertDto ) throws Exception {
        var user = userMapper.convertInsertDtoToEntity( userInsertDto );
        
        var optionalUserType = userTypeRepository.findByType( userInsertDto.type() );
        
        if(optionalUserType.isEmpty()){
            throw new Exception("O tipo de usuário inválido. Usuário deve ser Administrador, Professor ou Aluno");
        }
        
        user.setUserType( optionalUserType.get() );
        
        user.setPassword( passwordEncoder.encode( userInsertDto.password() ) );
        
        user.setMemoryGameSet( new HashSet<>() );
        user.setSubjectSet( new HashSet<>() );
        user.setScoreSet( new HashSet<>() );
        
        userRepository.save( user );
        
        return userRepository.save( user );
    }
    
    public UserEntity getCurrentUser() {
        return (UserEntity) SecurityContextHolder.getContext()
                                                 .getAuthentication()
                                                 .getPrincipal();
    }
    
}