package com.tcc.app.web.memory_game.api.application.utils;

import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.GameplayEntity;
import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
import com.tcc.app.web.memory_game.api.application.repositories.CodeGameplayRepository;
import com.tcc.app.web.memory_game.api.application.repositories.PlayerGameplayRepository;
import com.tcc.app.web.memory_game.api.custom.Pair;
import com.tcc.app.web.memory_game.api.infrastructures.security.entities.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class GameplayUtil {
    
    private static final Random random = new Random();
    
    @Autowired
    private CodeGameplayRepository codeGameplayRepository;
    
    @Autowired
    private PlayerGameplayRepository playerGameplayRepository;
    
    @Autowired
    private static final List<Pair<String,LocalDateTime>> codeList = new LinkedList<>();
    
    public CodeGameplayEntity getCodeGameplay(String code) throws Exception {
        return codeGameplayRepository.findByCode(code)
                                     .orElseThrow(() -> new EntityNotFoundException(
                                             "Não foi criado um gameplay ou código foi expirado!"));
    }
    
    public PlayerGameplayEntity getPlayerGameplay(UserEntity player, GameplayEntity gameplay) throws Exception {
        return playerGameplayRepository.findByPlayerAndGameplay(player, gameplay)
                                       .orElseThrow(() -> new EntityNotFoundException(
                                               "O jogador não foi encontrado para este gameplay!"));
    }
    
    public String generateCode() {
        StringBuilder codeBuilder = new StringBuilder();
        String code = null;
        
        char[] charList = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                           'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                           '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        
        boolean contains = true;
        while (contains) {
            for (int i = 0; i < 4; ++ i) {
                int indexRandom = random.nextInt(charList.length);
                codeBuilder.append(charList[indexRandom]);
            }
            
            code = codeBuilder.toString();
            
            String finalCode = code;
            contains = codeList.stream().anyMatch(code1 -> code1.v1().equals(finalCode));
        }
    
        codeList.add(new Pair<>(code, LocalDateTime.now().plusHours(2)));
        
        codeList.removeAll(codeList.stream()
                                   .filter(code1 -> LocalDateTime.now().isAfter(code1.v2()))
                                   .toList());
    
        return code;
    }
}