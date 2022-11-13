package com.tcc.jogodememoria.backend.teacher.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
public class TeacherDto {

    @NotEmpty
    private Set<String> subjectSet;

    @NotBlank
    private String email;
}
