package com.tcc.jogodememoria.backend.controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcc.jogodememoria.backend.dtos.card.CardDto;
import com.tcc.jogodememoria.backend.dtos.memorygame.MemoryGameDto;
import com.tcc.jogodememoria.backend.dtos.memorygame.MemoryGameUpdateDto;
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

@RestController
@RequestMapping("/memory-game")
public class MemoryGameController {

    final IMemoryGameService memoryGameService;
    final IUserService userService;
    final ISubjectService subjectService;
    final ICardService cardService;

    MemoryGameController(final IMemoryGameService memoryGameService,
            final IUserService userService,
            final ISubjectService subjectService,
            final ICardService cardService) {
        this.memoryGameService = memoryGameService;
        this.userService = userService;
        this.subjectService = subjectService;
        this.cardService = cardService;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Object> save(@RequestBody @Valid final MemoryGameDto memoryGameDto) {
        // Setando o nome do jogo da memória.
        MemoryGameModel memoryGame = new MemoryGameModel();

        final String memoryGameName = memoryGameDto.getName();
        memoryGame.setName(memoryGameName);

        // Verificando se usuário exsite.
        final String username = memoryGameDto.getUsername();
        final Optional<UserModel> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuário não encontrado.");
        }

        final UserModel user = optionalUser.get();
        // .ff
        final String userTypeName = user.getUserType().getType();
        // .fo
        if (!"Professor".equalsIgnoreCase(userTypeName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuário deve ser professor para ter permissão de criar jogo de memória.");
        }

        if (memoryGameService.existsByUserAndName(user, memoryGameName)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Jogo de memória já está adicionado.");
        }

        // Setando matérias para jogo de memória e adicionar na tabela subject.
        final Set<String> subjectNameSet = memoryGameDto.getSubjects();
        memoryGame.setSubjects(new HashSet<>());

        if (subjectNameSet != null) {

            final Set<SubjectModel> subjectSet = new HashSet<>();

            for (final String subjectName : subjectNameSet) {
            	if (!subjectService.existsByName(subjectName)) {
                    final SubjectModel subject = new SubjectModel(subjectName);
                    subjectSet.add(subjectService.save(subject));

                    continue;
                }

                final Optional<SubjectModel> opitionalSubject = subjectService.findByName(subjectName);

                if (opitionalSubject.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Por algum motivo não foi encontrado a matéria.");
                }

                subjectSet.add(opitionalSubject.get());
            }

            user.setSubjects(subjectSet);
            memoryGame.setSubjects(subjectSet);
        }

        memoryGame.setUser(user);
        
        memoryGame = memoryGameService.save(memoryGame);

        // Setando as cartas para jogo de memória e adicionar na tabela card.
        final Set<CardDto> cardDtoSet = memoryGameDto.getCards();
        if (cardDtoSet != null) {
			for (final CardDto cardDto : cardDtoSet) { 
				final String question = cardDto.getQuestion(); 
				final String answer = cardDto.getAnswer();
			  
				cardService.save(new CardModel(question, answer, memoryGame));
			}
        }
        

        return ResponseEntity.status(HttpStatus.OK)
                .body("Jogo de memória adicionado com sucesso.");
    }

    @PutMapping("/{username}/{memory-game-name}")
    @Transactional
    public ResponseEntity<Object> update(
            @PathVariable(value = "username") final String username,
            @PathVariable(value = "memory-game-name") final String memoryGameName,
            @RequestBody final MemoryGameUpdateDto memoryGameUpdateDto) {
        final Optional<UserModel> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Usuário não encontrado.");
        }

        final UserModel user = optionalUser.get();
        // .ff
        final Optional<MemoryGameModel> optionalMemoryGame = memoryGameService.findByUserAndName(user, memoryGameName);
        // .fo
        if (optionalMemoryGame.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Jogo de memória não encontrado.");
        }

        if (CustomBeanUtils.isAllNullProperty(memoryGameUpdateDto)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Nenhum dado foi fornecido para atualização do jogo de memória.");
        }
        
        MemoryGameModel memoryGame = optionalMemoryGame.get();
        
        final String newMemoryGameName = memoryGameUpdateDto.getName();
        if (newMemoryGameName != null) {
            memoryGame.setName(newMemoryGameName);
        }

        final Set<String> subjectNameSet = memoryGameUpdateDto.getSubjects();
        if (subjectNameSet != null) {
            final Set<SubjectModel> subjectSet = new HashSet<>();

            for (final String subjectName : subjectNameSet) {
                if (!subjectService.existsByName(subjectName)) {
                    final SubjectModel subject = new SubjectModel(subjectName);
                    subjectSet.add(subjectService.save(subject));

                    continue;
                }

                final Optional<SubjectModel> opitionalSubject = subjectService.findByName(subjectName);

                if (opitionalSubject.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Por algum motivo não foi encontrado a matéria.");
                }

                subjectSet.add(opitionalSubject.get());
            }

            user.setSubjects(subjectSet);
            memoryGame.setSubjects(subjectSet);
        }

        memoryGame.setUser(user);

        memoryGame = memoryGameService.save(memoryGame);

        // Setando as cartas para jogo de memória e adicionar na tabela card.
        final Set<CardDto> cardDtoSet = memoryGameUpdateDto.getCards();
        if (cardDtoSet != null) {
        	final List<CardModel> cardList = cardService.findByMemoryGame(memoryGame);
        	cardService.deleteAllInBatch(cardList);
        	
			for (final CardDto cardDto : cardDtoSet) { 
				final String question = cardDto.getQuestion(); 
				final String answer = cardDto.getAnswer();
			  
				cardService.save(new CardModel(question, answer, memoryGame));
			}
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Jogo de memória atualizado com sucesso.");
    }

    @GetMapping
    @Transactional
    public ResponseEntity<Object> getAll() {
        final List<MemoryGameModel> memoryGameList = memoryGameService.findAll();

        if (memoryGameList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body("Não foram encontrados jogos de memória.");
        }

        final List<MemoryGameResponse> memoryGameResponseList = new ArrayList<>();
        for (final MemoryGameModel memoryGame : memoryGameList) {
            // .ff
            final String memoryGameName = memoryGame.getName();
            final String username = memoryGame.getUser().getUsername();
            final Set<SubjectModel> subjectSet = memoryGame.getSubjects();
            final Set<CardModel> cardSet = memoryGame.getCards();
            // .fo
            if (CustomBeanUtils.someObjectIsNull(new Object[] { memoryGameName, username, subjectSet, cardSet })) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Deu algum erro!");
            }

            final MemoryGameResponse memoryGameResponse = new MemoryGameResponse(username, memoryGameName);

            memoryGameResponse.setSubjects(subjectSet.stream()
                    .map(SubjectModel::getName)
                    .collect(Collectors.toSet()));

            memoryGameResponse.setCards(cardSet.stream()
                    .map(card -> new CardResponse(card.getQuestion(), card.getAnswer()))
                    .collect(Collectors.toSet()));

            memoryGameResponseList.add(memoryGameResponse);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(memoryGameResponseList);
    }

    @GetMapping("/{username}")
    @Transactional
    public ResponseEntity<Object> getMemoryGameListByUsername(
            @PathVariable(value = "username") final String username) {

        final Optional<UserModel> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado usuário com este nome de usuário.");
        }

        final List<MemoryGameModel> memoryGameList = memoryGameService.findByUser(optionalUser.get());
        final List<MemoryGameResponse> memoryGameResponseList = new ArrayList<>();

        for (final MemoryGameModel memoryGame : memoryGameList) {
            final MemoryGameResponse memoryGameResponse = new MemoryGameResponse();
            memoryGameResponse.setUsername(username);
            memoryGameResponse.setName(memoryGame.getName());

            final Set<CardModel> cardSet = memoryGame.getCards();
            Set<CardResponse> cardsResponse = new HashSet<>();
            if (cardSet != null) {
                cardsResponse = cardSet.stream()
                        .map(card -> new CardResponse(card.getQuestion(), card.getAnswer()))
                        .collect(Collectors.toSet());
            }

            memoryGameResponse.setCards(cardsResponse);

            final Set<SubjectModel> subjectSet = memoryGame.getSubjects();
            Set<String> subjectsResp = new HashSet<>();
            if (subjectSet != null) {
                subjectsResp = subjectSet.stream()
                        .map(SubjectModel::getName)
                        .collect(Collectors.toSet());
            }

            memoryGameResponse.setSubjects(subjectsResp);

            memoryGameResponseList.add(memoryGameResponse);
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(memoryGameResponseList);
    }

    @GetMapping("/{username}/memory-games-name")
    @Transactional
    public ResponseEntity<Object> getMemoryGamesNameByUsername(
            @PathVariable(value = "username") final String username) {
        final Optional<UserModel> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado usuário com este nome de usuário.");
        }

        final List<MemoryGameModel> memoryGameList = memoryGameService.findByUser(optionalUser.get());
        final List<String> memoryGameNameSet = memoryGameList.stream()
                .map(MemoryGameModel::getName)
                .toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(memoryGameNameSet);
    }

    @GetMapping("/{username}/{memory-game-name}")
    @Transactional
    public ResponseEntity<Object> getByUsernameAndMemoryGameName(
            @PathVariable(value = "username") final String username,
            @PathVariable(value = "memory-game-name") final String memoryGameName) {

        final Optional<UserModel> optionalUser = userService.findByUsername(username);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado usuário com este nome de usuário.");
        }

        final Optional<MemoryGameModel> optionalMemoryGame = memoryGameService.findByUserAndName(optionalUser.get(),
                memoryGameName);

        if (optionalMemoryGame.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Não foi encontrado jogo de memória.");
        }

        final MemoryGameModel memoryGame = optionalMemoryGame.get();

        final MemoryGameResponse memoryGameResponse = new MemoryGameResponse();
        memoryGameResponse.setUsername(username);
        memoryGameResponse.setName(memoryGameName);

        final Set<CardModel> cardList = memoryGame.getCards();
        Set<CardResponse> cardResponseSet = new HashSet<>();
        if (cardList != null) {
            cardResponseSet = cardList.stream()
                    .map(card -> new CardResponse(card.getQuestion(), card.getAnswer()))
                    .collect(Collectors.toSet());
        }

        memoryGameResponse.setCards(cardResponseSet);

        final Set<SubjectModel> subjectSet = memoryGame.getSubjects();
        Set<String> subjectResponseSet = new HashSet<>();
        if (subjectSet != null) {
            subjectResponseSet = subjectSet.stream()
                    .map(SubjectModel::getName)
                    .collect(Collectors.toSet());
        }

        memoryGameResponse.setSubjects(subjectResponseSet);

        return ResponseEntity.status(HttpStatus.OK)
                .body(memoryGameResponse);
    }
}