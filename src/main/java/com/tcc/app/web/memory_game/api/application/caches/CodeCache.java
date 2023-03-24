package com.tcc.app.web.memory_game.api.application.caches;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public final class CodeCache {
    
    private final List<String> codeCache = new LinkedList<>();
    
    private final int limit = 50;
    
    public CodeCache add(String code) {
        if(codeCache.contains(code)) return this;
        if (codeCache.size() >= limit) codeCache.remove(0);
        codeCache.add(code);
        
        return this;
    }
    
    public boolean contains(String code) {
        return codeCache.contains(code);
    }
}