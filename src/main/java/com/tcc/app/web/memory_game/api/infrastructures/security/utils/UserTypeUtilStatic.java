package com.tcc.app.web.memory_game.api.infrastructures.security.utils;

import com.tcc.app.web.memory_game.api.infrastructures.security.enums.UserTypeEnum;

public final class UserTypeUtilStatic {
    
    private UserTypeUtilStatic() {}
    
    public static UserTypeEnum getType(String type) {
        return switch (type.toLowerCase()) {
            case "prof", "professor", "criador" -> UserTypeEnum.CRIADOR;
            case "aluno", "estudante", "jogador" -> UserTypeEnum.JOGADOR;
            default -> null;
        };
    }
}