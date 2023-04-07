package com.tcc.app.web.memory_game.api.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record PlayerScoreRequestDto(@Min(0) Integer score){
                                    //@NotEmpty Set<CardScoreRequestDto> cardSet) {
    
//    public Integer getNumberPairCardCorrect() {
//        long count = cardSet.stream().filter(cardScoreRequestDto -> cardScoreRequestDto.winner())
//                            .count();
//
//        return Integer.valueOf(count + "");
//    }
//
//    public Integer getNumberPairCardWrong() {
//        long count = cardSet.stream().filter(cardScoreRequestDto -> !cardScoreRequestDto.winner())
//                            .count();
//
//        return Integer.valueOf(count + "");
//    }
}