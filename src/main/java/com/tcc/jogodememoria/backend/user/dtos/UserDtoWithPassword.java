package com.tcc.jogodememoria.backend.user.dtos;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDtoWithPassword extends UserDto {

  @NotBlank
  private String password;
}
