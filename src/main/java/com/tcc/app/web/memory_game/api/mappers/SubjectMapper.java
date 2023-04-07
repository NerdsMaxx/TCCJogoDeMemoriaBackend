package com.tcc.app.web.memory_game.api.mappers;

import com.tcc.app.web.memory_game.api.entities.SubjectEntity;
import org.mapstruct.Mapper;

@Mapper
public interface SubjectMapper {
    
    default String toSubjectString(SubjectEntity subject) {
        return (subject != null) ? subject.getSubject() : null;
    }
}