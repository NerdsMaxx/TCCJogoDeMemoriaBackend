CREATE TABLE code_gameplay (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR NOT NULL UNIQUE,
    numbers_player_moment INT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    gameplay_id BIGINT NOT NULL UNIQUE,
    CONSTRAINT fk_gameplay
            FOREIGN KEY (gameplay_id)
                REFERENCES gameplay(id)
)