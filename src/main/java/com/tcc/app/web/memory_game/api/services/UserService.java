package com.tcc.app.web.memory_game.api.services;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.tcc.app.web.memory_game.api.custom.CustomException;
import com.tcc.app.web.memory_game.api.dtos.requests.ResetPasswordRequestDto;
import com.tcc.app.web.memory_game.api.dtos.requests.UserRequestDto;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import com.tcc.app.web.memory_game.api.entities.UserTypeEntity;
import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import com.tcc.app.web.memory_game.api.mappers.UserMapper;
import com.tcc.app.web.memory_game.api.repositories.UserRepository;
import com.tcc.app.web.memory_game.api.repositories.UserTypeRepository;
import com.tcc.app.web.memory_game.api.utils.UserTypeUtilStatic;
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
        UserEntity user = (UserEntity) SecurityContextHolder.getContext()
                                                     .getAuthentication()
                                                     .getPrincipal();
        return userRepository.findById(user.getId())
                             .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }
    
    public UserEntity findByUsername(String username) throws Exception {
        return userRepository.findByUsernameOrEmail(username);
    }
    
    public UserEntity findCreatorByUsernameOrEmail(String usernameOrEmail) throws CustomException {
        return userRepository.findCreatorByUsernameOrEmail(usernameOrEmail)
                             .orElseThrow(() -> new CustomException("Criador não foi encontrado!"));
    }
    
    public UserEntity findPlayerByUsernameOrEmail(String usernameOrEmail) throws CustomException {
        return userRepository.findPlayerByUsernameOrEmail(usernameOrEmail)
                             .orElseThrow(() -> new CustomException("Jogador não foi encontrado!"));
    }
    
    public UserEntity getCurrentCreator() throws Exception {
        UserEntity user = getCurrentUser();
        _throwIfUserIsNotCreator(user);
        return user;
    }
    
    public Optional<UserEntity> getCurrentOptionalCreator() throws Exception {
        UserEntity user = getCurrentUser();
        return (user.isCreator()) ? Optional.of(user) : Optional.empty();
    }
    
    public UserEntity getCurrentPlayer() throws Exception {
        UserEntity user = getCurrentUser();
        _throwIfUserIsNotPlayer(user);
        return user;
    }
    
    private void _throwIfUserIsNotPlayer(UserEntity user) throws CustomException {
        if(! user.isPlayer()) {
            throw new CustomException("Este usuário não é jogador.");
        }
    }
    
    private void _throwIfUserIsNotCreator(UserEntity user) throws CustomException {
        if(! user.isCreator()) {
            throw new CustomException("Este usuário não é criador.");
        }
    }
}