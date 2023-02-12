package com.tcc.app.web.memory_game.api.infrastructures.security.utils;

import com.tcc.app.web.memory_game.api.application.entities.CreatorEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerEntity;
import com.tcc.app.web.memory_game.api.application.services.CreatorService;
import com.tcc.app.web.memory_game.api.application.services.PlayerService;
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
    
    @Autowired
    private CreatorService creatorService;
    
    @Autowired
    private PlayerService playerService;
    
    public CreatorEntity getCurrentCreator() throws Exception {
        UserEntity user = userService.getCurrentUser();
        return creatorService.findByUser(user);
    }
    
    public Optional<CreatorEntity> getCurrentOptionalCreator() throws Exception {
        UserEntity user = userService.getCurrentUser();
        return creatorService.findOptionalByUser(user);
    }
    
    public PlayerEntity getCurrentPlayer() throws Exception {
        UserEntity user = userService.getCurrentUser();
        return playerService.findByUser(user);
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