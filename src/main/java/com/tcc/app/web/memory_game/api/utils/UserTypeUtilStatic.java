package com.tcc.app.web.memory_game.api.utils;

import com.tcc.app.web.memory_game.api.custom.CustomException;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;

public final class UserTypeUtilStatic {
    
    private UserTypeUtilStatic() {}
    
    public static UserTypeEnum getType(String type) {
        return switch (type.toLowerCase()) {
            case "prof", "professor", "criador" -> UserTypeEnum.CRIADOR;
            case "aluno", "estudante", "jogador" -> UserTypeEnum.JOGADOR;
            default -> null;
        };
    }
    
    public static void throwIfUserIsNotPlayer(UserEntity user) throws CustomException {
        if(! user.isPlayer()) {
            throw new CustomException("Este usuário não é jogador.");
        }
    }
    
    public static void throwIfUserIsNotCreator(UserEntity user) throws CustomException {
        if(! user.isCreator()) {
            throw new CustomException("Este usuário não é criador.");
        }
    }
}