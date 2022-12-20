package com.tcc.app.web.memory_game.api.application.interfaces;

public interface MapperInsertDtoToEntityInterface<T1, T2> {
		T2 convertInsertDtoToEntity(T1 objectDto);
}
