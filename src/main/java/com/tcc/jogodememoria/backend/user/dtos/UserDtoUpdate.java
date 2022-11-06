package com.tcc.jogodememoria.backend.user.dtos;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Setter
@Getter
public class UserDtoUpdate {

  @Nullable
  private String completeName;

  @Nullable
  private String username;

  @Nullable
  private String email;

  @Nullable
  private String password;

  @Nullable
  private String school;

  @Nullable
  private String course;
}
