CREATE TABLE assessment (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     TEXT,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE scale (
    id              BIGSERIAL PRIMARY KEY,
    assessment_id   BIGINT NOT NULL REFERENCES assessment(id),
    code            VARCHAR(50) NOT NULL,
    name            VARCHAR(255) NOT NULL,
    description     TEXT
);

CREATE TABLE item (
    id                  BIGSERIAL PRIMARY KEY,
    assessment_id       BIGINT NOT NULL REFERENCES assessment(id),
    scale_id            BIGINT NOT NULL REFERENCES scale(id),
    item_number         INT NOT NULL,
    text                TEXT NOT NULL,
    is_required         BOOLEAN NOT NULL DEFAULT TRUE,
    is_reverse_scored   BOOLEAN NOT NULL DEFAULT FALSE,
    weight              NUMERIC(5,2) NOT NULL DEFAULT 1.0,
    sort_order          INT NOT NULL
);

CREATE TABLE choice (
    id          BIGSERIAL PRIMARY KEY,
    item_id     BIGINT NOT NULL REFERENCES item(id),
    label       VARCHAR(255) NOT NULL,
    value       INT NOT NULL,
    sort_order  INT NOT NULL
);

CREATE TABLE norm (
    id              BIGSERIAL PRIMARY KEY,
    assessment_id   BIGINT NOT NULL REFERENCES assessment(id),
    scale_id        BIGINT REFERENCES scale(id),
    group_code      VARCHAR(50),
    mean            NUMERIC(10,4) NOT NULL,
    sd              NUMERIC(10,4) NOT NULL
);

CREATE TABLE response_session (
    id                  UUID PRIMARY KEY,
    assessment_id       BIGINT NOT NULL REFERENCES assessment(id),
    group_code          VARCHAR(50),
    started_at          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    submitted_at        TIMESTAMP WITHOUT TIME ZONE,
    is_completed        BOOLEAN NOT NULL DEFAULT FALSE,
    client_ip_masked    VARCHAR(64),
    user_agent          VARCHAR(512)
);

CREATE TABLE item_response (
    id                  BIGSERIAL PRIMARY KEY,
    response_session_id UUID NOT NULL REFERENCES response_session(id),
    item_id             BIGINT NOT NULL REFERENCES item(id),
    raw_value           INT NOT NULL,
    scored_value        NUMERIC(5,2),
    weighted_score      NUMERIC(7,2)
);

CREATE TABLE result (
    id                  UUID PRIMARY KEY,
    response_session_id UUID NOT NULL REFERENCES response_session(id),
    total_raw_score     NUMERIC(7,2),
    total_t_score       NUMERIC(7,2),
    scale_raw_scores    JSONB,
    scale_t_scores      JSONB,
    summary_text        TEXT,
    created_at          TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

