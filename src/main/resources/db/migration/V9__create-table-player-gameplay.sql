CREATE TABLE player_gameplay (
--    id BIGSERIAL PRIMARY KEY,
    score INT NOT NULL,
--    correct INT NOT NULL,
--    wrong INT NOT NULL,
    player_id BIGINT NOT NULL,
    gameplay_id BIGINT NOT NULL,
    CONSTRAINT fk_player_id
        FOREIGN KEY (player_id)
            REFERENCES user_mg(id),
    CONSTRAINT fk_gameplay_id
            FOREIGN KEY (gameplay_id)
                REFERENCES gameplay(id),
    CONSTRAINT pk_player_gameplay
            PRIMARY KEY (player_id, gameplay_id)
--    CONSTRAINT unique_player_gameplay
--        UNIQUE (player_id, gameplay_id)
)