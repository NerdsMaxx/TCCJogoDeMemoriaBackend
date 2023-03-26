package com.tcc.app.web.memory_game.api.application.mappers;

import com.tcc.app.web.memory_game.api.application.dtos.responses.MemoryGameCardsResponseDto;
import com.tcc.app.web.memory_game.api.application.dtos.responses.MemoryGameResponseDto;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {SubjectMapper.class, CardMapper.class})
public interface MemoryGameMapper {
    
    @Mapping(source = "memoryGame", target = "name")
    @Mapping(source = "creator.username", target = "creator")
    MemoryGameResponseDto toMemoryGameResponseDto(MemoryGameEntity memoryGame);
    
    @Mapping(source = "memoryGame", target = "name")
    @Mapping(source = "creator.username", target = "creator")
    MemoryGameCardsResponseDto toMemoryGameCardsResponseDto(MemoryGameEntity memoryGame);
}

//public abstract class MemoryGameMapper {
//    public static MemoryGameResponseDto toResponseDto(MemoryGameEntity memoryGame) {
//        if (memoryGame == null) {return null;}
//
//        CreatorEntity creator = memoryGame.getCreator();
//        if (creator == null) {return null;}
//
//        UserEntity user = creator.getUser();
//        if (user == null) {return null;}
//
//        String creatorUsername = user.getUsername();
//
//        String memoryGameName = memoryGame.getMemoryGame();
//
//        Set<SubjectEntity> subjectEntitiySet = memoryGame.getSubjectSet();
//        if (subjectEntitiySet == null) {return null;}
//
//        Set<String> subjectSet = subjectEntitiySet.stream()
//                                                  .map(SubjectEntity::getSubject)
//                                                  .collect(Collectors.toSet());
//
//        return new MemoryGameResponseDto(creatorUsername, memoryGameName, subjectSet);
//    }
//}