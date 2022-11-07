package com.tcc.jogodememoria.backend.teacher_subject.services;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.interfaces.ITeacherSubjectRepository;
import com.tcc.jogodememoria.backend.teacher_subject.interfaces.ITeacherSubjectService;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;
import com.tcc.jogodememoria.backend.teacher_subject.primary_key.TeacherSubjectId;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class TeacherSubjectService implements ITeacherSubjectService {

    TeacherSubjectService(ITeacherSubjectRepository teacherSubjectRepository) {
        this.teacherSubjectRepository = teacherSubjectRepository;
    }

    private final ITeacherSubjectRepository teacherSubjectRepository;

    @Transactional
    @Override
    public TeacherSubjectModel save(TeacherSubjectModel teacherSubjectModel) {
        return teacherSubjectRepository.save(teacherSubjectModel);
    }


    @Override
    public boolean existsByTeacherModelAndSubjectModel(
            TeacherModel teacherModel,
            SubjectModel subjectModel
    ) {
        return teacherSubjectRepository.existsById(new TeacherSubjectId(teacherModel.getId(), subjectModel.getId()));
    }

    @Override
    public List<TeacherSubjectModel> findAll() {
        return teacherSubjectRepository.findAll();
    }


    @Override
    public Optional<TeacherSubjectModel> findByTeacherModelAndSubjectModel(
            TeacherModel teacherModel,
            SubjectModel subjectModel
    ) {
        return teacherSubjectRepository.findById(new TeacherSubjectId(teacherModel.getId(), subjectModel.getId()));
    }

    @Transactional
    @Override
    public void delete(TeacherSubjectModel teacherSubjectModel) {
        teacherSubjectRepository.delete(teacherSubjectModel);
    }
}
