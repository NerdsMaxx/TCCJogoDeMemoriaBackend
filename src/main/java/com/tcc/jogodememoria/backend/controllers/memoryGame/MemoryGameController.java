package com.tcc.jogodememoria.backend.controllers.memoryGame;

import com.tcc.jogodememoria.backend.dtos.CardDto;
import com.tcc.jogodememoria.backend.dtos.MemoryGameDto;
import com.tcc.jogodememoria.backend.features.card.interfaces.ICardService;
import com.tcc.jogodememoria.backend.features.card.models.CardModel;
import com.tcc.jogodememoria.backend.features.memoryGame.interfaces.IMemoryGameService;
import com.tcc.jogodememoria.backend.features.memoryGame.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.features.subject.interfaces.ISubjectService;
import com.tcc.jogodememoria.backend.features.subject.models.SubjectModel;
import com.tcc.jogodememoria.backend.features.user.interfaces.IUserService;
import com.tcc.jogodememoria.backend.features.user.models.UserModel;
import com.tcc.jogodememoria.backend.responses.card.CardResponse;
import com.tcc.jogodememoria.backend.responses.memoryGame.MemoryGameResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/memoryGame")
public class MemoryGameController {
    
    MemoryGameController (IMemoryGameService memoryGameServ, IUserService userServ, ISubjectService subjectServ,
                          ICardService cardServ) {
        this.memoryGameServ = memoryGameServ;
        this.userServ = userServ;
        this.subjectServ = subjectServ;
        this.cardServ = cardServ;
    }
    
    final IMemoryGameService memoryGameServ;
    final IUserService userServ;
    final ISubjectService subjectServ;
    final ICardService cardServ;
    
    @PostMapping
    @Transactional
    public ResponseEntity<String> saveMemoryGame (@RequestBody @Valid MemoryGameDto memoryGameDto) {
        // Setando o nome do jogo da memória.
        MemoryGameModel memoryGame = new MemoryGameModel();
        
        String gameName = memoryGameDto.getName();
        memoryGame.setName(gameName);
        
        // Verificando se usuário exsite.
        final String username = memoryGameDto.getUsername();
        final Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Usuário não encontrado.");
        }
        
        UserModel user = opUser.get();
        
        //.ff
		String type = user.getUserType().getType();
		//.fo
        
        if ( type.equalsIgnoreCase("Professor") ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Usuário deve ser professor para ter permissão de criar jogo de memória.");
        }
        
        if ( memoryGameServ.existsByUserAndName(user, gameName) ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Jogo de memória já está adicionado.");
        }
        
        // Setando matérias para jogo de memória e adicionar na tabela subject.
        Set<String> subjectNames = memoryGameDto.getSubjects();
        memoryGame.setSubjects(new HashSet<>());
        
        if ( subjectNames != null ) {
            Set<SubjectModel> subjects = new HashSet<>();
            for ( String name : subjectNames ) {
                if ( ! subjectServ.existsByName(name) ) {
                    SubjectModel subject = new SubjectModel(name);
                    subjects.add(subjectServ.save(subject));
                    
                    continue;
                }
                
                Optional<SubjectModel> opSubject = subjectServ.findByName(name);
                
                if ( opSubject.isEmpty() ) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                                         .body("Por algum motivo não foi encontrado a matéria.");
                }
                
                subjects.add(opSubject.get());
            }
            
            user.setSubjects(subjects);
            memoryGame.setSubjects(subjects);
        }
        
        memoryGame.setUser(user);
        
        memoryGame = memoryGameServ.save(memoryGame);
        
        // Setando as cartas para jogo de memória e adicionar na tabela card.
        Set<CardDto> cardDtos = memoryGameDto.getCards();
        if ( cardDtos != null ) {
            for ( CardDto cardDto : cardDtos ) {
                cardServ.save(new CardModel(cardDto.getQuestion(), cardDto.getAnswer(), memoryGame));
            }
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body("Jogo de memória adicionado com sucesso.");
    }
    
    @GetMapping
    @Transactional
    public ResponseEntity<Object> getAll () {
        List<MemoryGameModel> memoryGames = memoryGameServ.findAll();
        
        if ( memoryGames.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("Não foram encontrados jogos de memória.");
        }
        
        List<MemoryGameResponse> memoryGameResps = new ArrayList<>();
        for ( MemoryGameModel memoryGame : memoryGames ) {
            MemoryGameResponse memoryGameResp = new MemoryGameResponse();
            
            //.ff
            memoryGameResp.setName(memoryGame.getName());
			
            memoryGameResp.setUsername(memoryGame.getUser().getUsername());
            
			memoryGameResp.setSubjects(memoryGame.getSubjects()
                                                 .stream()
                                                 .map(SubjectModel::getName)
                                                 .collect(Collectors.toSet()));
            
			memoryGameResp.setCards(memoryGame.getCards()
                                              .stream()
                                              .map((card) -> new CardResponse(card.getQuestion(), card.getAnswer()))
                                              .collect(Collectors.toSet()));
            //.fo
            
            memoryGameResps.add(memoryGameResp);
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memoryGameResps);
    }
    
    @GetMapping("/{username}/{memoryGameName}")
    @Transactional
    public ResponseEntity<Object> getByUsernameAndMemoryGameName (@PathVariable(value = "username")
                                                                  String username,
                                                                  @PathVariable(value = "memoryGameName")
                                                                  String memoryGameName) {
        
        Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não foi encontrado usuário com este nome de usuário.");
        }
        
        Optional<MemoryGameModel> opMemoryGame = memoryGameServ.findByUserAndName(opUser.get(), memoryGameName);
        
        if ( opMemoryGame.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não foi encontrado jogo de memória.");
        }
        
        MemoryGameModel memoryGame = opMemoryGame.get();
        
        MemoryGameResponse memoryGameResp = new MemoryGameResponse();
        memoryGameResp.setUsername(username);
        memoryGameResp.setName(memoryGameName);
        
        Set<CardModel> cards = memoryGame.getCards();
        Set<CardResponse> cardsResp = new HashSet<>();
        if ( cards != null ) {
            cardsResp = cards.stream()
                             .map((card) -> new CardResponse(card.getQuestion(), card.getAnswer()))
                             .collect(Collectors.toSet());
        }
        
        memoryGameResp.setCards(cardsResp);
        
        Set<SubjectModel> subjects = memoryGame.getSubjects();
        Set<String> subjectsResp = new HashSet<>();
        if ( subjects != null ) {
            subjectsResp = subjects.stream()
                                   .map(SubjectModel::getName)
                                   .collect(Collectors.toSet());
        }
        
        memoryGameResp.setSubjects(subjectsResp);
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memoryGameResp);
    }
}
