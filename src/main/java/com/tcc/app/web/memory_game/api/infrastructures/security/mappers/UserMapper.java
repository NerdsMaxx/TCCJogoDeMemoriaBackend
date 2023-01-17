package com.tcc.app.web.memory_game.api.infrastructures.security.mappers;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserRequestDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.responses.UserResponseDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    
    @Mapping(source = "user.userType.type", target = "type")
    UserResponseDto toUserResponseDto(UserEntity user);
    
    UserEntity toUserEntity(UserRequestDto userRequestDto);
}