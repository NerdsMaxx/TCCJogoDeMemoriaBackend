package com.tcc.app.web.memory_game.api.infrastructures.security.mappers;

import org.springframework.stereotype.Component;

import com.tcc.app.web.memory_game.api.application.interfaces.MapperEntityToDetailsDtoInterface;
import com.tcc.app.web.memory_game.api.application.interfaces.MapperInsertDtoToEntityInterface;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserInsertDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.responses.UserDetailsDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.enums.UserTypeEnum;

@Component
public final class UserMapper implements MapperInsertDtoToEntityInterface<UserInsertDto, UserEntity>,
				MapperEntityToDetailsDtoInterface<UserEntity, UserDetailsDto> {

		public UserEntity convertInsertDtoToEntity( UserInsertDto userInsertDto ) {
				var user = new UserEntity();

				user.setName( userInsertDto.name() );
				user.setUsername( userInsertDto.username() );
				user.setEmail( userInsertDto.email() );

				var userType = getTypeCorrect( userInsertDto.type() );
				user.setUserType( userType );

				return user;
		}

		public UserDetailsDto convertEntityToDetailsDto( UserEntity user ) {

				return new UserDetailsDto( user.getId(),
																	 user.getName(),
																	 user.getUsername(),
																	 user.getEmail(),
																	 user.getUserType().getTypeStr() );
		}

		private UserTypeEnum getTypeCorrect( String type ) {
				if ( UserTypeEnum.ADMIN.getTypeStr().equalsIgnoreCase( type ) ) {
						return UserTypeEnum.ADMIN;
				}

				if ( UserTypeEnum.TEACHER.getTypeStr().equalsIgnoreCase( type ) ) {
						return UserTypeEnum.TEACHER;
				}

				if ( UserTypeEnum.STUDENT.getTypeStr().equalsIgnoreCase( type ) ) {
						return UserTypeEnum.STUDENT;
				}

				return null;
		}

}
