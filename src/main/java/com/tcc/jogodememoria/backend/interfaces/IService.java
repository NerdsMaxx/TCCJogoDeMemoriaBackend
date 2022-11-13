package com.tcc.jogodememoria.backend.interfaces;

public interface IService<Type> {
    Type save(Type object);

    void delete(Type object);
}
