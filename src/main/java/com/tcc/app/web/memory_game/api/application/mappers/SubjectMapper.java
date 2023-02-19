package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import org.mapstruct.Mapper;

@Mapper
public interface SubjectMapper {
    
    default String toSubjectString(SubjectEntity subject) {
        return (subject != null) ? subject.getSubject() : null;
    }
}