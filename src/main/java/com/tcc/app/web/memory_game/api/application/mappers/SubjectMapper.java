package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.responses.SubjectResponseDto;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    
    default String toSubjectString(SubjectEntity subject) {
        return (subject != null) ? subject.getSubject() : null;
    }
}