package com.tcc.app.web.memory_game.api.application.utils;

import java.util.List;
import java.util.function.Predicate;

public final class ListUtilStatic {
    
    private ListUtilStatic() {}
    
    public static <T> List<T> addIfNotExist(List<T> list, List<T> otherList) {
        for (var element : otherList) {
            if (! list.contains(element)) {
                list.add(element);
            }
        }
        
        return list;
    }
    
    public static <T> List<T> addElementIfNotExist(T element, List<T> list, Predicate<T> predicate) {
        if (list.stream().noneMatch(predicate)) {
            list.add(element);
        }
        return list;
    }
    
    public static <T> List<T> addElementIfNotExist(T element, List<T> list) {
        if (! list.contains(element)) {
            list.add(element);
        }
        
        return list;
    }
}