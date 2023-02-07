CREATE TABLE card (
  id BIGSERIAL PRIMARY KEY,
  first_content VARCHAR NOT NULL,
  second_content VARCHAR NOT NULL,
  memory_game_id BIGINT NOT NULL,
  CONSTRAINT fk_memory_game
    FOREIGN KEY (memory_game_id)
        REFERENCES memory_game (id),
  CONSTRAINT unique_first_content_second_content_memory_game
    UNIQUE (first_content, second_content, memory_game_id)
);