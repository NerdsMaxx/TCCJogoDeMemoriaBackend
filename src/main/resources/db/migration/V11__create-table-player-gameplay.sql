CREATE TABLE player_gameplay (
    id BIGSERIAL PRIMARY KEY,
    score INT NOT NULL,
    correct INT NOT NULL,
    wrong INT NOT NULL,
    player_id BIGINT NOT NULL,
    gameplay_id BIGINT NOT NULL,
    CONSTRAINT fk_memory_game
        FOREIGN KEY (memory_game_id)
            REFERENCES memory_game(id),
    CONSTRAINT unique_player_gameplay
        UNIQUE (player_id, gameplay_id)
)