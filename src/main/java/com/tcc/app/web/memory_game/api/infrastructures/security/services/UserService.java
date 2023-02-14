package com.tcc.app.web.memory_game.api.infrastructures.security.services;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.tcc.app.web.memory_game.api.application.services.CreatorService;
import com.tcc.app.web.memory_game.api.application.services.PlayerService;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserRequestDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserTypeEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.enums.UserTypeEnum;
import com.tcc.app.web.memory_game.api.infrastructures.security.mappers.UserMapper;
import com.tcc.app.web.memory_game.api.infrastructures.security.repositories.UserRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.repositories.UserTypeRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.UserTypeUtilStatic;
import jakarta.persistence.EntityExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserTypeRepository userTypeRepository;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private CreatorService creatorService;
    
    @Autowired
    private PlayerService playerService;
    

    public UserEntity save(UserEntity user) {
        return userRepository.save(user);
    }
    

    public UserEntity saveAndFlush(UserEntity user) {
        return userRepository.saveAndFlush(user);
    }
    

    public UserEntity saveUser(UserRequestDto userRequestDto) throws Exception {
        UserEntity user = userMapper.toUserEntity(userRequestDto);
        
        if(userRepository.findByUsernameOrEmail(userRequestDto.username()) != null) {
            throw  new EntityExistsException("Este usuário já está adicionado.");
        }
        
        UserTypeEnum type = UserTypeUtilStatic.getType(userRequestDto.type());
        if(type == null){
            throw new InvalidValueException("O tipo de usuário inválido. Usuário deve ser Professor ou Aluno");
        }
        
        UserTypeEntity userType = userTypeRepository.findByType(type).orElseThrow();
        
        user.setUserType(userType);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        userRepository.save(user);
        
        if (userType.isCreator()) {
            creatorService.save(user);
        }
        
        playerService.saveByUser(user);
        
        return user;
    }
    
    public UserEntity getCurrentUser() throws Exception {
        var user = (UserEntity) SecurityContextHolder.getContext()
                                                     .getAuthentication()
                                                     .getPrincipal();
        return userRepository.findById(user.getId())
                             .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }
    
    public UserEntity findByUsername(String username) throws Exception {
        return userRepository.findByUsernameOrEmail(username);
    }
}