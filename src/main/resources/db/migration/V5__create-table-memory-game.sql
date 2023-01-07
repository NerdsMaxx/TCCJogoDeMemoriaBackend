CREATE TABLE memory_game (
  id BIGSERIAL PRIMARY KEY,
  memory_game VARCHAR NOT NULL,
  user_id BIGINT NOT NULL,
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
        REFERENCES user_mg(id),
  CONSTRAINT unique_memory_game_user
    UNIQUE (memory_game, user_id)
);