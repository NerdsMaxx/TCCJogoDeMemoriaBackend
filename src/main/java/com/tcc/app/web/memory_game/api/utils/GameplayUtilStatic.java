package com.tcc.app.web.memory_game.api.utils;

import com.tcc.app.web.memory_game.api.custom.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public final class GameplayUtilStatic {
    
    private static final Random random = new Random();
    private static final List<Pair<String,LocalDateTime>> codeList = new ArrayList<>();
    private static final char[] charList = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    
    private GameplayUtilStatic() {}
    
    public static String generateCode() {
        final StringBuilder codeBuilder = new StringBuilder();
        String code = "";
        
        boolean contains;
        do {
            for (int i = 0; i < 4; ++ i) {
                int indexRandom = random.nextInt(charList.length);
                codeBuilder.append(charList[indexRandom]);
            }
            
            code = codeBuilder.toString();
            
            final String finalCode = code;
            contains = codeList.stream().anyMatch(code1 -> code1.v1().equals(finalCode));
        } while (contains);
        
        codeList.add(new Pair<>(code, LocalDateTime.now().plusHours(2)));
        codeList.removeIf(code1 -> LocalDateTime.now().isAfter(code1.v2()));
        
        return code;
    }
}