CREATE TABLE player_gameplay (
    player_id BIGINT NOT NULL,
    gameplay_id BIGINT NOT NULL,
    score INT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    CONSTRAINT fk_player_id
        FOREIGN KEY (player_id)
            REFERENCES user_mg(id),
    CONSTRAINT fk_gameplay_id
        FOREIGN KEY (gameplay_id)
            REFERENCES gameplay(id),
    CONSTRAINT pk_player_gameplay
        PRIMARY KEY (player_id, gameplay_id)
)