package com.tcc.app.web.memory_game.api.infrastructures.security.utils;

import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public final class AuthenticatedUserUtil {
    @Autowired
    private UserService userService;
    
    //    @Autowired
//    private CreatorService creatorService;
//
//    @Autowired
//    private PlayerService playerService;
//
    public UserEntity getCurrentUser() throws Exception {
        return userService.getCurrentUser();
    }
    
    public UserEntity getCurrentCreator() throws Exception {
        UserEntity user = userService.getCurrentUser();
        UserTypeUtilStatic.throwIfUserIsNotCreator(user);
        return user;
    }
    
    public Optional<UserEntity> getCurrentOptionalCreator() throws Exception {
        UserEntity user = userService.getCurrentUser();
        return (user.isCreator()) ? Optional.of(user) : Optional.empty();
    }
    
    public UserEntity getCurrentPlayer() throws Exception {
        UserEntity user = userService.getCurrentUser();
        UserTypeUtilStatic.throwIfUserIsNotPlayer(user);
        return user;
    }
    
    public boolean isPlayer() throws Exception {
        UserEntity user = userService.getCurrentUser();
        return user.isPlayer();
    }
    
    public boolean isCreator() throws Exception {
        UserEntity user = userService.getCurrentUser();
        return user.isCreator();
    }
}