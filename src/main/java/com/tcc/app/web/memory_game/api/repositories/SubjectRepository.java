package com.tcc.app.web.memory_game.api.repositories;

import com.tcc.app.web.memory_game.api.entities.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SubjectRepository extends JpaRepository<SubjectEntity,Long> {
    @Query("SELECT sub " +
           "FROM SubjectEntity sub " +
           "WHERE sub.subject in  :subjectSet")
    Set<SubjectEntity> findBySubjectSet(Set<String> subjectSet);
}