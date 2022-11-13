package com.tcc.jogodememoria.backend.teacher.controllers;

import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.dtos.TeacherDto;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherController;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherService;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import com.tcc.jogodememoria.backend.utils.CustomBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/teacher")
public class TeacherController implements ITeacherController {

    private static final String TEACHER_NOT_FOUND_WARNING_STR = "Professor não foi encontrado.";

    public TeacherController(ITeacherService teacherService) {
        this.teacherService = teacherService;
    }

    final ITeacherService teacherService;

    @Override
    @PostMapping
    @Transactional
    public ResponseEntity<Object> saveTeacher(
            @RequestBody @Valid TeacherDto teacherDto) {


        final String email = teacherDto.getEmail();
        if (teacherService.existsUserByEmail(email)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Professor já está adicionado.");
        }

        Optional<UserModel> userModelOptional = teacherService.findUserByEmail(email);

        if (userModelOptional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Não existe usuário com este e-mail.");
        }

        TeacherModel teacherModel = new TeacherModel();

        teacherModel.setUserModel(userModelOptional.get());
        teacherModel.setSubjectModelSet(new HashSet<>());

        for (String subject : teacherDto.getSubjectSet()) {
            Optional<SubjectModel> optionalSubjectModel = teacherService.findSubjectModelByName(subject);

            if (optionalSubjectModel.isPresent()) {
                SubjectModel subjectModel = optionalSubjectModel.get();
                teacherModel.getSubjectModelSet().add(subjectModel);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        "A matéria " + subject
                                + " não existe. Deve adicionar matéria primeiro antes de adicionar"
                                + " o professor com tal matéria.");

            }
        }

        teacherService.saveTeacherModel(teacherModel);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Professor adicionado com sucesso.");
    }

    @Override
    @GetMapping
    public ResponseEntity<Object> getAllTeachers() {
        List<String> teacherModelList = teacherService.findAll()
                .stream().map(TeacherModel::toString).toList();

        return ResponseEntity.status(HttpStatus.OK).body(teacherModelList);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Object> getATeacher(
            @PathVariable(value = "id") Long id) {
        Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);

        if (optionalTeacherModel.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(TEACHER_NOT_FOUND_WARNING_STR);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(optionalTeacherModel.get().toString());
    }

    @Override
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> updateATeacher(
            @PathVariable(value = "id") Long id,
            @RequestBody TeacherDto teacherDto
    ) {
        if (CustomBeanUtils.isAllNullProperty(teacherDto)) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Nenhum dado foi fornecido para atualização do professor.");
        }

        Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);

        if (optionalTeacherModel.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(TEACHER_NOT_FOUND_WARNING_STR);
        }

        TeacherModel teacherModel = new TeacherModel();
        BeanUtils.copyProperties(optionalTeacherModel.get(), teacherModel);

        final String email = teacherDto.getEmail();
        if (email != null) {
            Optional<UserModel> optionalUserModel = teacherService.findUserByEmail(email);

            if (optionalUserModel.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Não existe usuário com este e-mail.");
            }

            teacherModel.setUserModel(optionalUserModel.get());
        }

        final Set<String> subjectSet = teacherDto.getSubjectSet();
        if (subjectSet != null) {

            for (String subject : subjectSet) {
                if (!teacherService.existsSubjectModelByName(subject)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(
                            "A matéria " + subject
                                    + " não existe. Deve adicionar matéria primeiro antes de atualizar"
                                    + " o professor com tal matéria.");
                }
            }

            teacherModel.setSubjectModelSet(new HashSet<>());

            for (String subject : subjectSet) {
                Optional<SubjectModel> optionalSubjectModel = teacherService.findSubjectModelByName(subject);

                if (optionalSubjectModel.isPresent()) {
                    SubjectModel subjectModel = optionalSubjectModel.get();
                    teacherModel.getSubjectModelSet().add(subjectModel);
                }
            }
        }

        teacherService.saveTeacherModel(teacherModel);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Professor atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Object> deleteATeacher(
            @PathVariable(value = "id") Long id) {
        Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);

        if (optionalTeacherModel.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(TEACHER_NOT_FOUND_WARNING_STR);
        }

        teacherService.deleteTeacherModel(optionalTeacherModel.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Usuário deletado com sucesso.");
    }
}
