CREATE TABLE creator (
    id BIGSERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
            REFERENCES user_mg(id)
);