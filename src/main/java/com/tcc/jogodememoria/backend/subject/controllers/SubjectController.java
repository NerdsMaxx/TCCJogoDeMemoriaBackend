package com.tcc.jogodememoria.backend.subject.controllers;

import com.tcc.jogodememoria.backend.subject.dtos.SubjectDto;
import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectController;
import com.tcc.jogodememoria.backend.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.subject.models.SubjectModel;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/subject")
public class SubjectController implements ISubjectController {

    public SubjectController(ISubjectService subjectService) {
        this.subjectService = subjectService;
    }

    private final ISubjectService subjectService;

    @Override
    @PostMapping
    public ResponseEntity<Object> saveSubject(
            @RequestBody @Valid SubjectDto subjectDto
    ) {
        if (subjectService.existsBySubjectName(subjectDto.getSubjectName())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("A matéria já está adicionado.");
        }

        SubjectModel subjectModel = new SubjectModel();
        BeanUtils.copyProperties(subjectDto, subjectModel);

        subjectService.saveSubjectModel(subjectModel);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Matéria adicionado com sucesso.");
    }

    @Override
    @GetMapping
    public ResponseEntity<Object> getAllSubjects() {
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.findAll());
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Object> getASubject(
            @PathVariable(value = "id") Long id
    ) {
        Optional<SubjectModel> optionalSubejctModel = subjectService.findById(id);

        if (optionalSubejctModel.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado a matéria.");
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(optionalSubejctModel.get());
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateASubject(
            @PathVariable(value = "id") Long id,
            @RequestBody @Valid SubjectDto subjectDto
    ) {
        Optional<SubjectModel> optionalSubejctModel = subjectService.findById(id);

        if (optionalSubejctModel.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado a matéria.");
        }

        SubjectModel subjectModel = new SubjectModel();
        BeanUtils.copyProperties(optionalSubejctModel.get(), subjectModel);
        BeanUtils.copyProperties(subjectDto, subjectModel);

        subjectService.saveSubjectModel(subjectModel);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Matéria atualizado com sucesso.");
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteASubject(@PathVariable(value = "id") Long id) {
        Optional<SubjectModel> optionalSubejctModel = subjectService.findById(id);

        if (optionalSubejctModel.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado a matéria.");
        }

        subjectService.deleteSubjectModel(optionalSubejctModel.get());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Matéria deletada com sucesso.");
    }
}
