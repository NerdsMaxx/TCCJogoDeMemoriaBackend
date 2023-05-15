package com.tcc.app.web.memory_game.api.utils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public final class CollectionUtil {

    private CollectionUtil() {}
    
    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }
    
    public static <T> boolean isNotEmpty(Collection<T> collection) {
        return ! isEmpty(collection);
    }
    
    public static <T> Optional<T> find(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).findFirst();
    }
    
    public static <T> T findOrNull(Collection<T> collection, Predicate<T> predicate) {
        return find(collection, predicate).orElse(null);
    }
    
    public static <T, A, R> R filter(Collection<T> collection, Predicate<T> predicate, Collector<T, A, R> collector) {
        return collection.stream().filter(predicate).collect(collector);
    }
}