package com.tcc.jogodememoria.backend.teacher.services;

import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherRepository;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherService;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.user.interfaces.IUserService;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherService implements ITeacherService {

    TeacherService(
            ITeacherRepository teacherRepository,
            IUserService userService,
            ISubjectService subjectService
    ) {
        this.teacherRepository = teacherRepository;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    final ITeacherRepository teacherRepository;
    final ISubjectService subjectService;
    final IUserService userService;

    @Override
    @Transactional
    public TeacherModel saveTeacherModel(TeacherModel teacherModel) {
        return teacherRepository.save(teacherModel);
    }

    @Override
    public boolean existsByUserModel(UserModel userModel) {
        return teacherRepository.existsByUserModel(userModel);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        Optional<UserModel> optionalUserModel = userService.findByEmail(email);

        if (optionalUserModel.isEmpty()) {
            return false;
        }

        return existsByUserModel(optionalUserModel.get());
    }

    @Override
    public boolean existsSubjectModelByName(String subjectName) {
        return subjectService.existsBySubjectName(subjectName);
    }

    @Override
    public List<TeacherModel> findAll() {
        return teacherRepository.findAll();
    }

    @Override
    public Optional<TeacherModel> findById(Long id) {
        return teacherRepository.findById(id);
    }

    @Override
    public Optional<TeacherModel> findByUserModel(UserModel userModel) {
        return teacherRepository.findByUserModel(userModel);
    }

    @Override
    public Optional<UserModel> findUserByEmail(String email) {
        return userService.findByEmail(email);
    }

    @Override
    public Optional<SubjectModel> findSubjectModelByName(String subjectName) {
        return subjectService.findBySubjectName(subjectName);
    }

    @Override
    @Transactional
    public void deleteTeacherModel(TeacherModel teacherModel) {
        teacherRepository.delete(teacherModel);
    }
}
