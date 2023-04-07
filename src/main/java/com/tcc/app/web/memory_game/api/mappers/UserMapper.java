package com.tcc.app.web.memory_game.api.mappers;

import com.tcc.app.web.memory_game.api.dtos.requests.UserRequestDto;
import com.tcc.app.web.memory_game.api.dtos.responses.UserResponseDto;
import com.tcc.app.web.memory_game.api.entities.UserEntity;
import com.tcc.app.web.memory_game.api.entities.UserTypeEntity;
import com.tcc.app.web.memory_game.api.enums.UserTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface UserMapper {
    
    @Mapping(expression = "java(toTypeResponseDto(user.getUserType()))", target = "type")
    UserResponseDto toUserResponseDto(UserEntity user);
    
    UserEntity toUserEntity(UserRequestDto userRequestDto);
    
    default Set<String> toTypeResponseDto(Set<UserTypeEntity> type) {
        return type.stream().map(UserTypeEntity::getType)
                   .map(UserTypeEnum::toString)
                   .collect(Collectors.toSet());
    }
}