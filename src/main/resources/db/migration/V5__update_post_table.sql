ALTER TABLE post
    DROP COLUMN thumb;

ALTER TABLE image
    DROP FOREIGN KEY fk_image_to_post;

ALTER TABLE image
    ADD CONSTRAINT fk_image_to_post
        FOREIGN KEY (post_id)
            REFERENCES post (id) ON DELETE CASCADE;

-- 데이터 일관성 정리
DELETE FROM image
    WHERE post_id NOT IN (SELECT id FROM post);