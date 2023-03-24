CREATE TABLE gameplay (
    id BIGSERIAL PRIMARY KEY,
    alone BOOLEAN NOT NULL,
    numbers_player INT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    memory_game_id BIGINT NOT NULL,
    CONSTRAINT fk_memory_game
            FOREIGN KEY (memory_game_id)
                REFERENCES memory_game(id)
)