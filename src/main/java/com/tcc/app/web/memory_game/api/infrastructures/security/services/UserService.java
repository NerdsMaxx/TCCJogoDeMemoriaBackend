package com.tcc.app.web.memory_game.api.infrastructures.security.services;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserRequestDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.mappers.UserMapper;
import com.tcc.app.web.memory_game.api.infrastructures.security.repositories.UserRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.repositories.UserTypeRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.UserTypeUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
    
    @Transactional
    public UserEntity saveAndFlush(UserEntity user) {
        return userRepository.saveAndFlush(user);
    }
    
    @Transactional
    public UserEntity saveUser(UserRequestDto userRequestDto) throws Exception {
        var user = userMapper.toUserEntity(userRequestDto);
        
        var type = UserTypeUtil.getType(userRequestDto.type());
        var userType = userTypeRepository.findByType(type)
                                         .orElseThrow(() -> new InvalidValueException(
                                                 "O tipo de usuário inválido. Usuário deve ser Administrador, Professor ou Aluno"));
        
        
        user.setUserType(userType);
        
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        
        userRepository.save(user);
        
        return userRepository.save(user);
    }
    
    public UserEntity getCurrentUser() throws Exception {
        var user = (UserEntity) SecurityContextHolder.getContext()
                                                     .getAuthentication()
                                                     .getPrincipal();
        return userRepository.findById(user.getId()).orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado!"));
    }
    
}