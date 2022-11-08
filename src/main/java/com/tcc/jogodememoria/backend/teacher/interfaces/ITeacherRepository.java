package com.tcc.jogodememoria.backend.teacher.interfaces;

import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ITeacherRepository extends JpaRepository<TeacherModel, Long> {
    boolean existsByUserModel(UserModel userModel);

    Optional<TeacherModel> findByUserModel(UserModel userModel);
}
