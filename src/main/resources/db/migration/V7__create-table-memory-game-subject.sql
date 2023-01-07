CREATE TABLE memory_game_subject (
    memory_game_id BIGINT NOT NULL,
    subject_id BIGINT NOT NULL,
      CONSTRAINT pk_memory_game_subject
        PRIMARY KEY (memory_game_id, subject_id),
      CONSTRAINT fk_memory_game
        FOREIGN KEY (memory_game_id)
            REFERENCES memory_game(id),
      CONSTRAINT fk_subject
        FOREIGN KEY (subject_id)
            REFERENCES subject(id)
);