package com.tcc.jogodememoria.backend.teacher.dtos;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeacherDto {

  @NotEmpty
  private List<String> subjects;

  @NotBlank
  private String email;
}
