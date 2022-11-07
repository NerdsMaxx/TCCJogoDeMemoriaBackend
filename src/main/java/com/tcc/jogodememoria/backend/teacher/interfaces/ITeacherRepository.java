package com.tcc.jogodememoria.backend.teacher.interfaces;

import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ITeacherRepository extends JpaRepository<TeacherModel, UUID> {
  boolean existsByUserModel(UserModel userModel);

  Optional<TeacherModel> findByUserModel(UserModel userModel);
}
