package com.tcc.jogodememoria.backend.dtos;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CardDto {
    @NotBlank
    private String question;

    @NotBlank
    private String answer;
}
