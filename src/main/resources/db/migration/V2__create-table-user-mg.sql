CREATE TABLE user_mg (
    id BIGSERIAL PRIMARY KEY,
    user_type_id INT NOT NULL,
    name VARCHAR NOT NULL,
    email VARCHAR NOT NULL UNIQUE,
    username VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    CONSTRAINT fk_user_type
        FOREIGN KEY(user_type_id)
            REFERENCES user_type(id)
);