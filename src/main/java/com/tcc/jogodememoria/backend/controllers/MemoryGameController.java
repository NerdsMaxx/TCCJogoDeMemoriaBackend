package com.tcc.jogodememoria.backend.controllers;

import com.tcc.jogodememoria.backend.dtos.CardDto;
import com.tcc.jogodememoria.backend.dtos.MemoryGameDto;
import com.tcc.jogodememoria.backend.interfaces.services.ICardService;
import com.tcc.jogodememoria.backend.interfaces.services.IMemoryGameService;
import com.tcc.jogodememoria.backend.interfaces.services.ISubjectService;
import com.tcc.jogodememoria.backend.interfaces.services.IUserService;
import com.tcc.jogodememoria.backend.models.CardModel;
import com.tcc.jogodememoria.backend.models.MemoryGameModel;
import com.tcc.jogodememoria.backend.models.SubjectModel;
import com.tcc.jogodememoria.backend.models.UserModel;
import com.tcc.jogodememoria.backend.responses.card.CardResponse;
import com.tcc.jogodememoria.backend.responses.memoryGame.MemoryGameResponse;
import com.tcc.jogodememoria.backend.utils.CustomBeanUtils;
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
    
    final IMemoryGameService memoryGameServ;
    final IUserService userServ;
    final ISubjectService subjectServ;
    final ICardService cardServ;
    
    MemoryGameController (final IMemoryGameService memoryGameServ,
                          final IUserService userServ,
                          final ISubjectService subjectServ,
                          final ICardService cardServ) {
        this.memoryGameServ = memoryGameServ;
        this.userServ = userServ;
        this.subjectServ = subjectServ;
        this.cardServ = cardServ;
    }
    
    @PostMapping
    @Transactional
    public ResponseEntity<String> save (@RequestBody @Valid final MemoryGameDto memoryGameDto) {
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
        // .ff
        String type = user.getUserType().getType();
        // .fo
        if ( type.equalsIgnoreCase("Professor") ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Usuário deve ser professor para ter permissão de criar jogo de memória.");
        }
        
        if ( memoryGameServ.existsByUserAndName(user, gameName) ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Jogo de memória já está adicionado.");
        }
        
        // Setando matérias para jogo de memória e adicionar na tabela subject.
        final Set<String> subjectNames = memoryGameDto.getSubjects();
        memoryGame.setSubjects(new HashSet<>());
        
        if ( subjectNames != null ) {
            
            final Set<SubjectModel> subjects = new HashSet<>();
            
            for ( final String name : subjectNames ) {
                if ( ! subjectServ.existsByName(name) ) {
                    final SubjectModel subject = new SubjectModel(name);
                    subjects.add(subjectServ.save(subject));
                    
                    continue;
                }
                
                final Optional<SubjectModel> opSubject = subjectServ.findByName(name);
                
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
        final Set<CardDto> cardDtos = memoryGameDto.getCards();
        if ( cardDtos != null ) {
            for ( final CardDto cardDto : cardDtos ) {
                cardServ.save(new CardModel(cardDto.getQuestion(), cardDto.getAnswer(), memoryGame));
            }
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body("Jogo de memória adicionado com sucesso.");
    }
    
    @GetMapping
    @Transactional
    public ResponseEntity<Object> getAll () {
        final List<MemoryGameModel> memoryGames = memoryGameServ.findAll();
        
        if ( memoryGames.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.OK)
                                 .body("Não foram encontrados jogos de memória.");
        }
        
        List<MemoryGameResponse> memoryGameResps = new ArrayList<>();
        for ( final MemoryGameModel memoryGame : memoryGames ) {
            //.ff
            final String name = memoryGame.getName();
            final String username = memoryGame.getUser().getUsername();
            final Set<SubjectModel> subjects = memoryGame.getSubjects();
            final Set<CardModel> cards = memoryGame.getCards();
            //.fo
            if ( CustomBeanUtils.someObjectIsNull(new Object[]{name, username, subjects, cards}) ) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("Deu algum erro!");
            }
            
            MemoryGameResponse memoryGameResp = new MemoryGameResponse(username, name);
            
            memoryGameResp.setSubjects(subjects.stream()
                                               .map(SubjectModel::getName)
                                               .collect(Collectors.toSet()));
            
            memoryGameResp.setCards(cards.stream()
                                         .map((card) -> new CardResponse(card.getQuestion(), card.getAnswer()))
                                         .collect(Collectors.toSet()));
            
            memoryGameResps.add(memoryGameResp);
        }
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memoryGameResps);
    }
    
    @GetMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> getMemoryGameListByUsername (
            @PathVariable(value = "username") final String username) {
        
        final Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não foi encontrado usuário com este nome de usuário.");
        }
        
        final List<MemoryGameModel> memoryGames = memoryGameServ.findByUser(opUser.get());
        final List<MemoryGameResponse> memoryGamesResp = new ArrayList<>();
        
        for ( final MemoryGameModel memoryGame : memoryGames ) {
            final MemoryGameResponse memoryGameResp = new MemoryGameResponse();
            memoryGameResp.setUsername(username);
            memoryGameResp.setName(memoryGame.getName());
            
            final Set<CardModel> cards = memoryGame.getCards();
            Set<CardResponse> cardsResp = new HashSet<>();
            if ( cards != null ) {
                cardsResp = cards.stream()
                                 .map((card) -> new CardResponse(card.getQuestion(), card.getAnswer()))
                                 .collect(Collectors.toSet());
            }
            
            memoryGameResp.setCards(cardsResp);
            
            final Set<SubjectModel> subjects = memoryGame.getSubjects();
            Set<String> subjectsResp = new HashSet<>();
            if ( subjects != null ) {
                subjectsResp = subjects.stream()
                                       .map(SubjectModel::getName)
                                       .collect(Collectors.toSet());
            }
            
            memoryGameResp.setSubjects(subjectsResp);
            
            memoryGamesResp.add(memoryGameResp);
        }
        
        
        return ResponseEntity.status(HttpStatus.OK)
                             .body(memoryGamesResp);
    }
    
    @GetMapping("/{username}/{memoryGameName}")
    @Transactional
    public ResponseEntity<Object> getByUsernameAndMemoryGameName (
            @PathVariable(value = "username") final String username,
            @PathVariable(value = "memoryGameName") final String memoryGameName) {
        
        final Optional<UserModel> opUser = userServ.findByUsername(username);
        
        if ( opUser.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não foi encontrado usuário com este nome de usuário.");
        }
        
        final Optional<MemoryGameModel> opMemoryGame = memoryGameServ.findByUserAndName(opUser.get(), memoryGameName);
        
        if ( opMemoryGame.isEmpty() ) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                 .body("Não foi encontrado jogo de memória.");
        }
        
        final MemoryGameModel memoryGame = opMemoryGame.get();
        
        final MemoryGameResponse memoryGameResp = new MemoryGameResponse();
        memoryGameResp.setUsername(username);
        memoryGameResp.setName(memoryGameName);
        
        final Set<CardModel> cards = memoryGame.getCards();
        Set<CardResponse> cardsResp = new HashSet<>();
        if ( cards != null ) {
            cardsResp = cards.stream()
                             .map((card) -> new CardResponse(card.getQuestion(), card.getAnswer()))
                             .collect(Collectors.toSet());
        }
        
        memoryGameResp.setCards(cardsResp);
        
        final Set<SubjectModel> subjects = memoryGame.getSubjects();
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