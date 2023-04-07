package com.tcc.app.web.memory_game.api.services;

import com.tcc.app.web.memory_game.api.repositories.CodeGameplayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ScheduledTasksService {
    
    @Autowired
    private GameplayService gameplayService;
    
    @Scheduled(fixedRate = 7200000L) //7200000 millisegundos = 2 horas
    public void deleteCodeGameplayNotUsed() {
        gameplayService.deleteAllCodeInvalidated();
    }
}