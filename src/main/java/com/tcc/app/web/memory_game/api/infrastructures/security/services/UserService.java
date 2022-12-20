package com.tcc.app.web.memory_game.api.infrastructures.security.services;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.tcc.app.web.memory_game.api.infrastructures.security.dtos.requests.UserInsertDto;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import com.tcc.app.web.memory_game.api.infrastructures.security.mappers.UserMapper;
import com.tcc.app.web.memory_game.api.infrastructures.security.repositories.UserRepository;

@Service
public class UserService {

		@Autowired
		private UserRepository userRepository;

		@Autowired
		private UserMapper userMapper;

		@Autowired
		private PasswordEncoder passwordEncoder;

		public UserEntity registerNewUser( UserInsertDto userInsertDto ) throws Exception {
				var user = userMapper.convertInsertDtoToEntity( userInsertDto );
				if(user.getUserType() == null) {
						throw new Exception( "Não foi definido tipo corretamente. Tente novamente! As opções são: Admin, Professor e Aluno." );
				}
				
				user.setPassword( passwordEncoder.encode( userInsertDto.password() ) );
				
				user.setMemoryGameSet( new HashSet<>() );
				user.setSubjectSet( new HashSet<>() );
				user.setScoreSet( new HashSet<>() );
				
				userRepository.save( user );

				return userRepository.save( user );
		}

}
