CREATE TABLE user_subject (
  user_id BIGINT NOT NULL,
  subject_id BIGINT NOT NULL,
  CONSTRAINT pk_user_subject
    PRIMARY KEY (user_id, subject_id),
  CONSTRAINT fk_user
    FOREIGN KEY (user_id)
        REFERENCES user_mg(id),
  CONSTRAINT fk_subject
    FOREIGN KEY (subject_id)
        REFERENCES subject(id)
);