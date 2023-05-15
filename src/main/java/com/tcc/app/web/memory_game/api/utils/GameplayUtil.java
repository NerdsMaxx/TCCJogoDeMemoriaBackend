package com.tcc.app.web.memory_game.api.utils;

import com.tcc.app.web.memory_game.api.repositories.CodeGameplayRepository;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Scope("singleton")
public final class GameplayUtil {
    private final CodeGameplayRepository codeGameplayRepository;
    
    public GameplayUtil(CodeGameplayRepository codeGameplayRepository) {
        this.codeGameplayRepository = codeGameplayRepository;
    }
    
    private final Random random = new Random();
    private final char[] charList = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };
    
    public String generateCode() {
        final StringBuilder codeBuilder = new StringBuilder();
        String code = "";
        
        boolean contains;
        do {
            for (int i = 0; i < 4; ++ i) {
                int indexRandom = random.nextInt(charList.length);
                codeBuilder.append(charList[indexRandom]);
            }
            
            code = codeBuilder.toString();
        } while (codeGameplayRepository.existsByCode(code));
        
        return code;
    }
}