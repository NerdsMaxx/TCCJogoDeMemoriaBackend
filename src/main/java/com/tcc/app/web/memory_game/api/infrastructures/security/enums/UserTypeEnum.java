package com.tcc.app.web.memory_game.api.infrastructures.security.enums;

public enum UserTypeEnum {
    TEACHER{
        @Override
        public String getTypeStr(){
            return "Professor";
        }
    },
    STUDENT{
        @Override
        public String getTypeStr(){
            return "Aluno";
        }
    },
    ADMIN{
        @Override
        public String getTypeStr(){
            return "Admin";
        }
    };

    public abstract String getTypeStr();
}
