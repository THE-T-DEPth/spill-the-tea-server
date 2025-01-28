CREATE TABLE IF NOT EXISTS comment_liked (
    id                 BIGINT AUTO_INCREMENT        NOT NULL,
    member_id          BIGINT                       NOT NULL,
    comment_id         BIGINT                       NOT NULL,
    created_date       DATETIME                     NOT NULL,
    modified_date      DATETIME                     NOT NULL,
    CONSTRAINT pk_comment_liked PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE comment_liked
    ADD CONSTRAINT uq_comment_liked UNIQUE (member_id, comment_id);

ALTER TABLE comment_liked
    ADD CONSTRAINT fk_comment_liked_to_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE comment_liked
    ADD CONSTRAINT fk_comment_liked_to_comment
        FOREIGN KEY (comment_id)
            REFERENCES comment (id);