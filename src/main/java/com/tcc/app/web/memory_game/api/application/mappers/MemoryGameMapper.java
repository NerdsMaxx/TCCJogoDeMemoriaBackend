package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.responses.MemoryGameResponseDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemoryGameMapper {
    
    @Mapping(source = "memoryGame.memoryGame", target = "name")
    @Mapping(target = "subjectList", expression = "java(toSubjectNameList(memoryGame.getSubjectList()))")
    @Mapping(source = "memoryGame.user.userType.type", target = "user.type")
    MemoryGameResponseDto toMemoryGameResponseDto(MemoryGameEntity memoryGame);
    
    default List<String> toSubjectNameList(List<SubjectEntity> subjectList) {
        return subjectList.stream().map(SubjectEntity::getSubject).toList();
    }
}