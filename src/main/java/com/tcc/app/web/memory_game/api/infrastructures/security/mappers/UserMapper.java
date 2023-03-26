package com.tcc.app.web.memory_game.api.infrastructures.security.mappers;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserRequestDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.responses.UserResponseDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserTypeEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.enums.UserTypeEnum;
import com.tcc.app.web.memory_game.api.infrastructures.security.utils.UserTypeUtilStatic;
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