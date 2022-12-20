package com.tcc.app.web.memory_game.api.application.interfaces;

public interface MapperInterface<T1, T2, T3>{
		T3 convertInsertDtoToEntity(T1 objectDto) throws Exception;
		T2 convertEntityToDetailsDto(T3 objectDto);
}
