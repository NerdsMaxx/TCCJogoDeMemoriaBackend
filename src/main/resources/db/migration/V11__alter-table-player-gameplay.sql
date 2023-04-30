ALTER TABLE player_gameplay
ADD COLUMN number_right_options INT NOT NULL,
ADD COLUMN number_wrong_options INT NOT NULL,
ADD COLUMN number_attempts INT NOT NULL;