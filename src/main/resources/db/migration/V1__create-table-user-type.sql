CREATE TABLE user_type (
     id SERIAL PRIMARY KEY,
     type VARCHAR NOT NULL UNIQUE
);

INSERT INTO user_type(type) VALUES('Administrador');
INSERT INTO user_type(type) VALUES('Professor');
INSERT INTO user_type(type) VALUES('Aluno');