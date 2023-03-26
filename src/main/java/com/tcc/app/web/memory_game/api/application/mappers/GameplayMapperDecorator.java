//package com.tcc.app.web.memory_game.api.application.mappers;
//
//import com.tcc.app.web.memory_game.api.application.dtos.requests.CardScoreRequestDto;
//import com.tcc.app.web.memory_game.api.application.dtos.requests.PlayerScoreRequestDto;
//import com.tcc.app.web.memory_game.api.application.entities.CardGameplayEntity;
//import com.tcc.app.web.memory_game.api.application.entities.PlayerGameplayEntity;
//import com.tcc.app.web.memory_game.api.application.services.MemoryGameService;
//import org.mapstruct.MappingTarget;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//
//public abstract class GameplayMapperDecorator implements GameplayMapper {
//
//    @Autowired
//    @Qualifier("delegate")
//    private GameplayMapper delegate;
//
//    @Autowired
//    private MemoryGameService memoryGameService;
//
//    @Override
//    public PlayerGameplayEntity updatePlayerGameplay(PlayerScoreRequestDto playerScoreRequestDto,
//                                                     @MappingTarget
//                                                     PlayerGameplayEntity playerGameplay) throws Exception {
//        playerGameplay = delegate.updatePlayerGameplay(playerScoreRequestDto, playerGameplay);
//
////        for (CardScoreRequestDto cardScoreRequestDto : playerScoreRequestDto.cardSet()) {
////            CardGameplayEntity cardGameplay = playerGameplay.findCardGameplay(cardScoreRequestDto);
////            updateCardGameplay(cardScoreRequestDto, cardGameplay);
////        }
//
//        return playerGameplay;
//    }
//
////    @Override
////    public CardGameplayEntity updateCardGameplay(CardScoreRequestDto cardScoreRequestDto,
////                                                 @MappingTarget CardGameplayEntity cardGameplay) throws Exception {
////        cardGameplay = delegate.updateCardGameplay(cardScoreRequestDto, cardGameplay);
////        cardGameplay.setCard(memoryGameService.findCardById(cardScoreRequestDto.id()));
////
////        return cardGameplay;
////    }
//}