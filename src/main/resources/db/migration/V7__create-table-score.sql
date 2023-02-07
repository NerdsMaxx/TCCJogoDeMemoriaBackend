CREATE TABLE score (
    id BIGSERIAL PRIMARY KEY,
    score INT NOT NULL,
    memory_game_id BIGINT NOT NULL,
    CONSTRAINT fk_memory_game
        FOREIGN KEY (memory_game_id)
            REFERENCES memory_game(id),
    CONSTRAINT unique_score_memory_game
        UNIQUE (score, memory_game_id)
);