package com.tcc.jogodememoria.backend.teacher.controllers;

import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.teacher.dtos.TeacherDto;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherController;
import com.tcc.jogodememoria.backend.teacher.interfaces.ITeacherService;
import com.tcc.jogodememoria.backend.teacher.models.TeacherModel;
import com.tcc.jogodememoria.backend.teacher_subject.models.TeacherSubjectModel;
import com.tcc.jogodememoria.backend.teacher_subject.primary_key.TeacherSubjectId;
import com.tcc.jogodememoria.backend.user.models.UserModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/teacher")
public class TeacherController implements ITeacherController {

    private static final String TEACHER_NOT_FOUND_WARNING_STR = "Professor não foi encontrado.";

    public TeacherController(
            ITeacherService teacherService,
            ISubjectService subjectService) {
        this.teacherService = teacherService;
        this.subjectService = subjectService;
    }

    final ITeacherService teacherService;
    final ISubjectService subjectService;

    @Override
    @PostMapping
    public ResponseEntity<Object> saveTeacher(
            @RequestBody @Valid TeacherDto teacherDto) {

        if (teacherService.existsUserByEmail(teacherDto.getEmail())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Professor já está adicionado.");
        }

        Optional<UserModel> optionalUserModel = teacherService.findUserByEmail(teacherDto.getEmail());

        if (!optionalUserModel.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("Não existe usuário com este e-mail.");
        }

        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setUserModel(optionalUserModel.get());
        teacherModel = teacherService.save(teacherModel);

        for (String subject : teacherDto.getSubjects()) {
            Optional<SubjectModel> optionalSubjectModel = teacherService.findSubjectModelByName(
                    subject);

            if (optionalSubjectModel.isPresent()) {
                SubjectModel subjectModel = optionalSubjectModel.get();

                TeacherSubjectModel teacherSubjectModel = new TeacherSubjectModel();

                teacherSubjectModel.setTeacherSubjectId(new TeacherSubjectId(teacherModel.getId(), subjectModel.getId()));

                teacherService.saveTeacherSubjectModel(teacherSubjectModel);
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        "A matéria " + subject
                                + " não existe. Deve adicionar matéria primeiro antes de adicionar"
                                + " o professor com tal matéria.");

            }
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Professor adicionado com sucesso.");
    }

    @Override
    @GetMapping
    public ResponseEntity<Object> getAllTeachers() {
        return ResponseEntity.status(HttpStatus.OK).body(teacherService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Object> getATeacher(
            @PathVariable(value = "id") UUID id) {
        Optional<TeacherModel> teacherModelOptional = teacherService.findById(id);

        if (!teacherModelOptional.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(TEACHER_NOT_FOUND_WARNING_STR);
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(teacherModelOptional.get());
    }

//  @Override
//  @PutMapping("/{id}")
//  public ResponseEntity<Object> updateATeacher(
//          @PathVariable(value = "id") UUID id,
//          @RequestBody TeacherDto teacherDto
//  ) {
//    if (CustomBeanUtils.isAllNullProperty(teacherDto)) {
//      return ResponseEntity
//              .status(HttpStatus.CONFLICT)
//              .body("Nenhum dado foi fornecido para atualização do professor.");
//    }
//
//    Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);
//
//    if (!optionalTeacherModel.isPresent()) {
//      return ResponseEntity
//              .status(HttpStatus.CONFLICT)
//              .body(TEACHER_NOT_FOUND_WARNING_STR);
//    }
//
//    TeacherModel teacherModel = new TeacherModel();
//    BeanUtils.copyProperties(optionalTeacherModel.get(), teacherModel);
//
//    final String email = teacherDto.getEmail();
//    if (email != null) {
//      Optional<UserModel> optionalUserModel = teacherService.findUserByEmail(email);
//
//      if (!optionalUserModel.isPresent()) {
//        return ResponseEntity
//                .status(HttpStatus.CONFLICT)
//                .body("Não existe usuário com este e-mail.");
//      }
//
//      teacherModel.setUserModel(optionalUserModel.get());
//      teacherService.save(teacherModel);
//    }
//
//    final List<String> subjects = teacherDto.getSubjects();
//    if (subjects != null) {
//      for (String subject : subjects) {
//        Optional<SubjectModel> optionalSubjectModel = teacherService.findSubjectModelByName(subject);
//
//        if (optionalSubjectModel.isPresent()) {
//          SubjectModel subjectModel = optionalSubjectModel.get();
//
//          if (teacherService.existsTeacherSubjectModelByTeacherModelAndSubjectModel(teacherModel, subjectModel)) {
//            continue;
//          }
//
//          TeacherSubjectModel teacherSubjectModel = new TeacherSubjectModel();
//
//          //Deletar primeiro
//          teacherSubjectModel.setTeacherSubjectId(new TeacherSubjectId(teacherModel.getId(), subjectModel.getId()));
//          teacherService.deleteTeacherSubjectModel(teacherSubjectModel);
//
//          //Depois adicionar
//          teacherSubjectModel.setTeacherSubjectId(new TeacherSubjectId(teacherModel.getId(), subjectModel.getId()));
//          teacherService.saveTeacherSubjectModel(teacherSubjectModel);
//        } else {
//          return ResponseEntity.status(HttpStatus.CONFLICT).body(
//                  "A matéria " + subject
//                          + " não existe. Deve adicionar matéria primeiro antes de atualizar"
//                          + " o professor com tal matéria.");
//
//        }
//      }
//    }
//
//
//    return ResponseEntity
//            .status(HttpStatus.OK)
//            .body("Professor atualizado com sucesso.");
//  }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteATeacher(
            @PathVariable(value = "id") UUID id) {
        Optional<TeacherModel> optionalTeacherModel = teacherService.findById(id);

        if (!optionalTeacherModel.isPresent()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(TEACHER_NOT_FOUND_WARNING_STR);
        }

        teacherService.delete(optionalTeacherModel.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Usuário deletado com sucesso.");
    }
}
