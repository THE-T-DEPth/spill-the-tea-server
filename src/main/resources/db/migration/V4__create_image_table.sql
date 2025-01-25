CREATE TABLE IF NOT EXISTS image (
    id              BIGINT AUTO_INCREMENT    NOT NULL,
    post_id         BIGINT                   NULL,
    url             TEXT                     NOT NULL,
    thumb           BOOLEAN                  NOT NULL,
    created_date    DATETIME                 NOT NULL,
    modified_date   DATETIME                 NOT NULL,
    CONSTRAINT pk_post_keyword PRIMARY KEY (id)
) ENGINE=InnoDB;

ALTER TABLE image
    ADD CONSTRAINT fk_image_to_post
        FOREIGN KEY (post_id)
            REFERENCES post (id);