//package com.tcc.app.web.memory_game.api.application.caches;
//
//import org.springframework.beans.factory.config.ConfigurableBeanFactory;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
//public final class CodeCache {
//
//    private final List<String> codeList = new ArrayList<>();
//
//    public void add(String code) {
//        if(codeList.contains(code)) {
//            return;
//        }
//
//        int limit = 500;
//        if (codeList.size() >= limit) {
//
//            if(codeList.get(0))
//            codeList.remove(0);
//        }
//        codeList.add(code);
//
//    }
//
//    public boolean contains(String code) {
//        return codeList.contains(code);
//    }
//}