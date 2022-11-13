package com.tcc.jogodememoria.backend.dtos;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class UserDto {

    @NotBlank
    private String name;

    @NotBlank
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String type;
}
