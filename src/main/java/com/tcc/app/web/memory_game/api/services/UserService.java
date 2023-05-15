package com.tcc.app.web.memory_game.api.services;

import com.electronwill.nightconfig.core.conversion.InvalidValueException;
import com.tcc.app.web.memory_game.api.dtos.requests.ResetPasswordRequestDto;
import com.tcc.app.web.memory_game.api.dtos.requests.UserRequestDto;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import com.tcc.app.web.memory_game.api.entities.UserTypeEntity;
import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import com.tcc.app.web.memory_game.api.mappers.UserMapper;
import com.tcc.app.web.memory_game.api.repositories.UserRepository;
import com.tcc.app.web.memory_game.api.repositories.UserTypeRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.awt.color.ProfileDataException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserTypeRepository userTypeRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    
    
    public UserEntity save(UserRequestDto userRequestDto) {
       UserEntity user = userRepository.findByUsernameOrEmail(userRequestDto.username());
        if (user != null) {
            throw new EntityExistsException("Este usuário já está adicionado.");
        }
        
        user = userMapper.toUserEntity(userRequestDto);
        
        Set<UserTypeEnum> type = userRequestDto.type().stream()
                                               .map(UserTypeEnum::getType)
                                               .collect(Collectors.toSet());
        
        if (type.isEmpty() || type.contains(null)) {
            throw new InvalidValueException(
                    "O tipo de usuário não foi dado. Usuário deve ser Criador ou Jogador ou ambos.");
        }
        
        Set<UserTypeEntity> userType = type.stream()
                                           .map(type1 -> userTypeRepository.findByType(type1).orElseThrow())
                                           .collect(Collectors.toSet());
        
        user.addTypes(userType);
        user.setPassword(passwordEncoder.encode(userRequestDto.password()));
        userRepository.save(user);
        
        return user;
    }
    
    public UserEntity changePassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        UserEntity user = userRepository.findByUsernameOrEmail(resetPasswordRequestDto.username());
        
        if(passwordEncoder.matches(resetPasswordRequestDto.newPassword(), user.getPassword())){
            throw new ProfileDataException("A senha não pode ser igual a anterior!");
        }
        
        user.setPassword(passwordEncoder.encode(resetPasswordRequestDto.newPassword()));
        
        return userRepository.save(user);
    }
    
    public UserEntity getCurrentUser() {
        UserEntity user = (UserEntity) SecurityContextHolder.getContext()
                                                            .getAuthentication()
                                                            .getPrincipal();
        return userRepository.findById(user.getId())
                             .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado!"));
    }
    
    public UserEntity findCreatorByUsernameOrEmail(@NonNull String usernameOrEmail) {
        return userRepository.findCreatorByUsernameOrEmail(usernameOrEmail)
                             .orElseThrow(() -> new EntityNotFoundException("Criador não foi encontrado!"));
    }
    
    public UserEntity findPlayerByUsernameOrEmail(@NonNull String usernameOrEmail) {
        return userRepository.findPlayerByUsernameOrEmail(usernameOrEmail)
                             .orElseThrow(() -> new EntityNotFoundException("Jogador não foi encontrado!"));
    }
    
    public UserEntity getCurrentCreator() throws NoPermissionException {
        final UserEntity user = getCurrentUser();
        
        if (user.isNotCreator()) {
            throw new NoPermissionException("Este usuário não é criador.");
        }
        
        return user;
    }
    
    public UserEntity getCurrentPlayer() throws NoPermissionException {
        final UserEntity user = getCurrentUser();
        
        if (user.isNotPlayer()) {
            throw new NoPermissionException("Este usuário não é jogador.");
        }
        
        return user;
    }
}