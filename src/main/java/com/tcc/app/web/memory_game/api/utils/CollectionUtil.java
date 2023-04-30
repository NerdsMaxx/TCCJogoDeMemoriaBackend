package com.tcc.app.web.memory_game.api.utils;

import java.util.Collection;
import java.util.function.Predicate;

public final class CollectionUtil {

    private CollectionUtil() {}
    
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
    
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return ! isEmpty(collection);
    }
}