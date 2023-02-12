package com.tcc.app.web.memory_game.api.application.controllers;

import com.tcc.app.web.memory_game.api.application.mappers.SubjectMapper;
import com.tcc.app.web.memory_game.api.application.services.SubjectService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/materia")
@CrossOrigin("*")
public class SubjectController {
    
    @Autowired
    private SubjectService subjectService;
    
    @Autowired
    private SubjectMapper subjectMapper;
    
    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_CRIADOR') or #username == authentication.principal.username")
    public ResponseEntity getAllSubjectByPlayer(@NotBlank @PathVariable("username") String username) throws Exception {
        var subjectList = subjectService.findAllByPlayer(username);
        
        return ResponseEntity.ok(subjectList.stream()
                                            .map(subjectMapper::toSubjectString));
    }
}