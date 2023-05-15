package com.tcc.app.web.memory_game.api.mappers;

import com.tcc.app.web.memory_game.api.entities.SubjectEntity;
import org.mapstruct.Mapper;

import java.util.Objects;
import java.util.Optional;

@Mapper
public interface SubjectMapper {
    
    default String toSubjectString(SubjectEntity subject) {
        return (subject != null) ? subject.getSubject() : null;
    }
}