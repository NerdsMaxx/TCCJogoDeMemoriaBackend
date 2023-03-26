package com.tcc.app.web.memory_game.api.infrastructures.security.services;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.ResetPasswordRequestDto;
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

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
    
//    @Autowired
//    private CreatorService creatorService;
//
//    @Autowired
//    private PlayerService playerService;
    

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
        
        Set<UserTypeEnum> type = userRequestDto.type().stream()
                                               .map(UserTypeUtilStatic::getType)
                                               .collect(Collectors.toSet());
        
        if(type.isEmpty() || type.contains(null)){
            throw new InvalidValueException("O tipo de usuário não foi dado. Usuário deve ser Criador ou Jogador ou ambos.");
        }
        
        Set<UserTypeEntity> userType = type.stream()
                                           .map(type1 -> userTypeRepository.findByType(type1).orElseThrow())
                                           .collect(Collectors.toSet());
        
        user.setUserType(userType);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        userRepository.save(user);
        
        return user;
    }
    
    public UserEntity changePassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        UserEntity user = userRepository.findByUsernameOrEmail(resetPasswordRequestDto.username());
        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.newPassword()));
        
        return userRepository.save(user);
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
    
    public Optional<UserEntity> findCreatorByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findCreatorByUsernameOrEmail(usernameOrEmail);
    }
    
    public Optional<UserEntity> findPlayerByUsernameOrEmail(String usernameOrEmail) {
        return userRepository.findPlayerByUsernameOrEmail(usernameOrEmail);
    }
}