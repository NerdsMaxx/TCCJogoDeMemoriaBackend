package com.tcc.jogodememoria.backend.teacher.dtos;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDto {

  @NotBlank
  private String subjectName;

  @NotBlank
  private String email;
}
