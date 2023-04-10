package com.tcc.app.web.memory_game.api.custom;

import lombok.Data;
import lombok.NonNull;

@Data
public class SimpleValue<T> {
    @NonNull
    private T value;
}