-- V34 이후에도 테이블이 없는 경우(히스토리와 실제 스키마 불일치 등)를 복구한다.
CREATE TABLE IF NOT EXISTS feedback_post (
    id UUID NOT NULL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    body VARCHAR(8000) NOT NULL,
    author_display VARCHAR(64),
    created_at TIMESTAMP NOT NULL,
    hidden BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX IF NOT EXISTS idx_feedback_post_created ON feedback_post (created_at DESC);
CREATE INDEX IF NOT EXISTS idx_feedback_post_hidden_created ON feedback_post (hidden, created_at DESC);
