CREATE TABLE user_type (
     id SERIAL PRIMARY KEY,
     type VARCHAR NOT NULL UNIQUE
);

INSERT INTO user_type(type) VALUES('CRIADOR');
INSERT INTO user_type(type) VALUES('JOGADOR');