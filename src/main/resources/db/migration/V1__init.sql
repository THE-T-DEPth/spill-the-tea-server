CREATE TABLE IF NOT EXISTS member (
    id              BIGINT AUTO_INCREMENT   NOT NULL,
    email           VARCHAR(255)            NOT NULL,
    password        VARCHAR(255)            NOT NULL,
    name            VARCHAR(255)            NOT NULL,
    nickname        VARCHAR(255)            NOT NULL,
    profile_image   VARCHAR(255)            NOT NULL,
    created_date    DATETIME                NOT NULL,
    modified_date   DATETIME                NOT NULL,
    CONSTRAINT pk_member PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS post (
    id              BIGINT AUTO_INCREMENT       NOT NULL,
    member_id       BIGINT                      NOT NULL,
    title           VARCHAR(255)                NULL,
    content         TEXT                        NULL,
    thumb           TEXT                        NULL,
    liked_count     INT                         DEFAULT 0 CHECK (liked_count >= 0),
    comment_count   INT                         DEFAULT 0 CHECK (comment_count >= 0),
    reported_count  INT                         DEFAULT 0 CHECK (reported_count >=0),
    voice_type      ENUM ('MALE', 'FEMALE')     NULL,
    created_date    DATETIME                    NOT NULL,
    modified_date   DATETIME                    NOT NULL,
    CONSTRAINT pk_post PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS comment (
    id                 BIGINT AUTO_INCREMENT        NOT NULL,
    parent_comment_id  BIGINT                       NULL,
    member_id          BIGINT                       NOT NULL,
    post_id            BIGINT                       NOT NULL,
    content            TEXT                         NOT NULL,
    liked_count        INT                          DEFAULT 0 CHECK (liked_count >= 0),
    reported_count     INT                          DEFAULT 0 CHECK (reported_count >= 0),
    created_date       DATETIME                     NOT NULL,
    modified_date      DATETIME                     NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS keyword (
    id              BIGINT AUTO_INCREMENT    NOT NULL,
    name            VARCHAR(255)             NOT NULL,
    created_date    DATETIME                 NOT NULL,
    modified_date   DATETIME                 NOT NULL,
    CONSTRAINT pk_keyword PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS post_keyword (
    id              BIGINT AUTO_INCREMENT    NOT NULL,
    post_id         BIGINT                   NOT NULL,
    keyword_id      BIGINT                   NOT NULL,
    created_date    DATETIME                 NOT NULL,
    modified_date   DATETIME                 NOT NULL,
    CONSTRAINT pk_post_keyword PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS block (
    id              BIGINT AUTO_INCREMENT       NOT NULL,
    blocker_id      BIGINT                      NOT NULL,
    blocked_id      BIGINT                      NOT NULL,
    created_date    DATETIME                    NOT NULL,
    modified_date   DATETIME                    NOT NULL,
    CONSTRAINT pk_block PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS liked (
    id                 BIGINT AUTO_INCREMENT        NOT NULL,
    member_id          BIGINT                       NOT NULL,
    post_id            BIGINT                       NOT NULL,
    created_date       DATETIME                     NOT NULL,
    modified_date      DATETIME                     NOT NULL,
    CONSTRAINT pk_liked PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS post_report (
    id              BIGINT AUTO_INCREMENT    NOT NULL,
    member_id       BIGINT                   NOT NULL,
    post_id         BIGINT                   NOT NULL,
    created_date    DATETIME                 NOT NULL,
    modified_date   DATETIME                 NOT NULL,
    CONSTRAINT pk_post_report PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS comment_report (
    id              BIGINT AUTO_INCREMENT    NOT NULL,
    member_id       BIGINT                   NOT NULL,
    comment_id      BIGINT                   NOT NULL,
    created_date    DATETIME                 NOT NULL,
    modified_date   DATETIME                 NOT NULL,
    CONSTRAINT pk_post_report PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS refresh_token (
    email           VARCHAR(255)             NOT NULL,
    refresh_token   TEXT                     NOT NULL,
    created_date    DATETIME                 NOT NULL,
    modified_date   DATETIME                 NOT NULL,
    CONSTRAINT pk_refresh_token PRIMARY KEY (email)
) ENGINE=InnoDB;

ALTER TABLE member
    ADD CONSTRAINT uq_member_email UNIQUE (email);

ALTER TABLE liked
    ADD CONSTRAINT uq_likes UNIQUE (member_id, post_id);

ALTER TABLE keyword
    ADD CONSTRAINT uq_tag_name UNIQUE (name);

ALTER TABLE block
    ADD CONSTRAINT uq_block UNIQUE (blocker_id, blocked_id);

ALTER TABLE post_report
    ADD CONSTRAINT uq_post_report UNIQUE (member_id, post_id);

ALTER TABLE comment_report
    ADD CONSTRAINT uq_comment_report UNIQUE (member_id, comment_id);

# post
ALTER TABLE post
    ADD CONSTRAINT fk_post_to_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

# comment
ALTER TABLE comment
    ADD CONSTRAINT fk_comment_to_parent_comment
        FOREIGN KEY (parent_comment_id)
            REFERENCES comment (id);

ALTER TABLE comment
    ADD CONSTRAINT fk_comment_to_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE comment
    ADD CONSTRAINT fk_comment_to_post
        FOREIGN KEY (post_id)
            REFERENCES post (id);

# liked
ALTER TABLE liked
    ADD CONSTRAINT fk_liked_to_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE liked
    ADD CONSTRAINT fk_liked_to_post
        FOREIGN KEY (post_id)
            REFERENCES post (id);

# block
ALTER TABLE block
    ADD CONSTRAINT fk_block_to_member_1
        FOREIGN KEY (blocker_id)
            REFERENCES member (id);

ALTER TABLE block
    ADD CONSTRAINT fk_block_to_member_2
        FOREIGN KEY (blocked_id)
            REFERENCES member (id);

# post_keyword
ALTER TABLE post_keyword
    ADD CONSTRAINT fk_post_keyword_to_post
        FOREIGN KEY (post_id)
            REFERENCES post (id);

ALTER TABLE post_keyword
    ADD CONSTRAINT fk_post_keyword_to_keyword
        FOREIGN KEY (keyword_id)
            REFERENCES keyword (id);

# post_report
ALTER TABLE post_report
    ADD CONSTRAINT fk_post_report_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE post_report
    ADD CONSTRAINT fk_post_report_post
        FOREIGN KEY (post_id)
            REFERENCES post (id);

# comment_report
ALTER TABLE comment_report
    ADD CONSTRAINT fk_comment_report_member
        FOREIGN KEY (member_id)
            REFERENCES member (id);

ALTER TABLE comment_report
    ADD CONSTRAINT fk_comment_report_comment
        FOREIGN KEY (comment_id)
            REFERENCES comment (id);