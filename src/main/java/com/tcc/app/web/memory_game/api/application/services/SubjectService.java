package com.tcc.app.web.memory_game.api.application.services;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tcc.app.web.memory_game.api.application.entities.MemoryGameEntity;
import com.tcc.app.web.memory_game.api.application.entities.SubjectEntity;
import com.tcc.app.web.memory_game.api.application.repositories.SubjectRepository;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;

@Service
public class SubjectService {

		@Autowired
		private SubjectRepository subjectRepository;

		public Set<SubjectEntity> registerNewSubjects( Set<String> subjectStringSet, MemoryGameEntity memoryGame,
						UserEntity user ) {
				var subjectSet = new HashSet<SubjectEntity>();

				for ( var subjectString : subjectStringSet ) {
						var optionalSubject = subjectRepository.findByName( subjectString );
						SubjectEntity subject;

						if ( optionalSubject.isPresent() ) {
								subject = optionalSubject.get();
						} else {
								subject = new SubjectEntity( subjectString );
								subject.setUserSet( new HashSet<>() );
								subject.setMemoryGameSet( new HashSet<>() );
						}

//						var userSet = subject.getUserSet();
//						if ( !userSet.contains( user ) ) {
//								userSet.add( user );
//						}

//						subject.getMemoryGameSet().add( memoryGame );
//
//						if ( optionalSubject.isEmpty() ) {
//								subject = subjectRepository.save( subject );
//						}

						subjectSet.add( subject );
				}

				return subjectSet;
		}
}