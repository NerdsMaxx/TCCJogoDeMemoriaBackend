package com.tcc.app.web.memory_game.api.application.services;

import com.tcc.app.web.memory_game.api.application.entities.CodeGameplayEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScheduledTasksService {
    
    @Autowired
    private GameplayService gameplayService;
    
    @Scheduled(fixedRate = 7200000L) //2 horas
    private void _deleteCodeGameplayNotUsed() {
        List<CodeGameplayEntity> codeGameplayList = gameplayService.findAllCodeGameplay();
        List<CodeGameplayEntity> codeGameplayToDeleteList = new LinkedList<>();
        
        for(CodeGameplayEntity codeGameplay : codeGameplayList) {
            Calendar calendarStart = Calendar.getInstance();
            calendarStart.setTime(codeGameplay.getStartDate());
            
            Calendar calendarNow = Calendar.getInstance();
            
            if(calendarNow.getTimeInMillis() - calendarStart.getTimeInMillis() >= 7200000L) {
                codeGameplayToDeleteList.add(codeGameplay);
            }
        }
        
        gameplayService.deleteAllCodeGameplay(codeGameplayList);
    }
}