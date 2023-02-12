package com.tcc.app.web.memory_game.api.infrastructures.security.utils;

public final class UserTypeUtilStatic {
    
    private UserTypeUtilStatic(){}
    
    public static String getType(String type) {
        return switch ( type.toLowerCase() ){
            case "prof", "professor", "criador" -> "CRIADOR";
            case "aluno", "estudante", "jogador" -> "JOGADOR";
            default -> null;
        };
    }
}