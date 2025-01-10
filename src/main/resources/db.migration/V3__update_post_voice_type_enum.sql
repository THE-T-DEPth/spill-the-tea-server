-- 기존 voice_type ENUM 값 삭제
ALTER TABLE post DROP COLUMN voice_type;

-- 새로운 voice_type ENUM 추가: 기본값('none')
ALTER TABLE post
ADD COLUMN voice_type ENUM('none', 'ko_KR_Standard_A', 'ko_KR_Standard_C') NOT NULL DEFAULT 'none';
