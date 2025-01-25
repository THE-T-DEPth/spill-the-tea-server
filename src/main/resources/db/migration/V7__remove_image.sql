-- image 테이블 삭제
DROP TABLE IF EXISTS image;

-- post 테이블에 thumb_url 재생성
ALTER TABLE post
    ADD COLUMN thumb_url TEXT NULL;