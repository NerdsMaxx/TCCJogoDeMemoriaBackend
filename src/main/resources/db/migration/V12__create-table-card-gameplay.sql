CREATE TABLE card_gameplay (
    id BIGSERIAL PRIMARY KEY,
    winner BOOLEAN NOT NULL,
    player_gameplay_id BIGINT NOT NULL,
    card_id BIGINT NOT NULL,
    CONSTRAINT fk_player_gameplay_id
        FOREIGN KEY (player_gameplay_id)
            REFERENCES player_gameplay(id),
    CONSTRAINT fk_card_id
        FOREIGN KEY (card_id)
            REFERENCES card(id),
    CONSTRAINT unique_player_gameplay_card
        UNIQUE (player_gameplay_id, card_id)
)