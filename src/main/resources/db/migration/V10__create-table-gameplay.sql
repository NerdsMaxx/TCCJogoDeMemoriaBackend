CREATE TABLE gameplay (
    id BIGSERIAL PRIMARY KEY,
    numbers_player INT NOT NULL,
    memory_game_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_memory_game
            FOREIGN KEY (memory_game_id)
                REFERENCES memory_game(id)
)