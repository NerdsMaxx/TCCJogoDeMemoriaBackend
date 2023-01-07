CREATE TABLE score (
    id BIGSERIAL PRIMARY KEY,
    score INT NOT NULL,
    user_id BIGINT NOT NULL,
    memory_game_id BIGINT NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
            REFERENCES user_mg (id),
    CONSTRAINT fk_memory_game
        FOREIGN KEY (memory_game_id)
            REFERENCES memory_game(id),
    CONSTRAINT unique_score_user_memory_game
        UNIQUE (score, user_id, memory_game_id)
);