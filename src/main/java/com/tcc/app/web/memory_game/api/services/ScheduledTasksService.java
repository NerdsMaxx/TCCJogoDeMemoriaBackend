package com.tcc.app.web.memory_game.api.services;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ScheduledTasksService {
    private final GameplayService gameplayService;
    
    @Scheduled(fixedRate = 7200000L) //7200000 millisegundos = 2 horas
    public void deleteCodeGameplayNotUsed() {
        gameplayService.deleteAllCodeInvalidated();
    }
}