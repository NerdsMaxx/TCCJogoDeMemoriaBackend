CREATE TABLE user_type (
    user_id BIGINT NOT NULL,
    type_user_id BIGINT NOT NULL,
    CONSTRAINT fk_user
        FOREIGN KEY(user_id)
            REFERENCES user_mg(id),
    CONSTRAINT fk_type_user
        FOREIGN KEY(type_user_id)
            REFERENCES type_user(id)
);