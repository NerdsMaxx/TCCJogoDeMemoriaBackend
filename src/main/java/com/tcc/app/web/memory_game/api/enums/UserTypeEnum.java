package com.tcc.app.web.memory_game.api.enums;

public enum UserTypeEnum {
    CRIADOR,
    JOGADOR;
    
    public static UserTypeEnum getType(String type) {
        return switch (type.toLowerCase()) {
            case "prof", "professor", "criador" -> CRIADOR;
            case "aluno", "estudante", "jogador" -> JOGADOR;
            default -> null;
        };
    }
}