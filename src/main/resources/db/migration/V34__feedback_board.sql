CREATE TABLE feedback_post (
    id UUID NOT NULL PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    body VARCHAR(8000) NOT NULL,
    author_display VARCHAR(64),
    created_at TIMESTAMP NOT NULL,
    hidden BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE INDEX idx_feedback_post_created ON feedback_post (created_at DESC);
CREATE INDEX idx_feedback_post_hidden_created ON feedback_post (hidden, created_at DESC);
