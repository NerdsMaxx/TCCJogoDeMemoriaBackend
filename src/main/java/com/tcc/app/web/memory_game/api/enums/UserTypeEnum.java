package com.tcc.app.web.memory_game.api.enums;

import lombok.NonNull;

public enum UserTypeEnum {
    CRIADOR,
    JOGADOR;
    
    public static UserTypeEnum getType(@NonNull String type) {
        return switch (type.toLowerCase()) {
            case "prof", "professor", "criador" -> CRIADOR;
            case "aluno", "estudante", "jogador" -> JOGADOR;
            default -> null;
        };
    }
}