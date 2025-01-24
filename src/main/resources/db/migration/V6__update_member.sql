ALTER TABLE member
    DROP COLUMN profile_image;

ALTER TABLE member
    ADD COLUMN profile_image VARCHAR(255) NULL;