package com.tcc.app.web.memory_game.api.infrastructures.security.utils;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserTypeUtil {
    public static String getType(String type) {
        return switch ( type.toLowerCase() ){
            case "admin", "administrador" -> "Administrador";
            case "prof", "professor" -> "Professor";
            case "aluno", "estudante" -> "Aluno";
            default -> type;
        };
    }
}