package com.tcc.jogodememoria.backend.subject.dtos;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDto {

  @NotBlank
  private String subjectName;
}
