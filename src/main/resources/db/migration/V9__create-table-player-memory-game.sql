CREATE TABLE player_memory_game (
    player_id BIGINT NOT NULL,
    memory_game_id BIGINT NOT NULL,
      CONSTRAINT pk_player_memory_game
        PRIMARY KEY (player_id, memory_game_id),
      CONSTRAINT fk_player
        FOREIGN KEY (player_id)
            REFERENCES user_mg(id),
      CONSTRAINT fk_memory_game
        FOREIGN KEY (memory_game_id)
            REFERENCES memory_game(id)
);