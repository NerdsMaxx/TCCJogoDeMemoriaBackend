package com.tcc.jogodememoria.backend.user.dtos;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {

  @NotBlank
  private String completeName;

  @NotBlank
  private String username;

  @NotBlank
  private String email;

  @NotBlank
  private String school;

  @NotBlank
  private String course;
}
