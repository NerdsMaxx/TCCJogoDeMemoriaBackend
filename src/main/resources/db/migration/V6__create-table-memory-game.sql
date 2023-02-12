CREATE TABLE memory_game (
  id BIGSERIAL PRIMARY KEY,
  memory_game VARCHAR NOT NULL,
  creator_id BIGINT NOT NULL,
  CONSTRAINT fk_creator
    FOREIGN KEY (creator_id)
        REFERENCES creator(id),
  CONSTRAINT unique_memory_game_creator
    UNIQUE (memory_game, creator_id)
);