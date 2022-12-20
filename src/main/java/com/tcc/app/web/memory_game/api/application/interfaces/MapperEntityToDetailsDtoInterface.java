package com.tcc.app.web.memory_game.api.application.interfaces;

public interface MapperEntityToDetailsDtoInterface<T1, T2> {
		T2 convertEntityToDetailsDto(T1 objectEntity);
}
