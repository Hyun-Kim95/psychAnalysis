CREATE TABLE admin_user (
    id            BIGSERIAL PRIMARY KEY,
    login_id      VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role          VARCHAR(50) NOT NULL DEFAULT 'ADMIN',
    created_at    TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now()
);

CREATE TABLE access_log (
    id               BIGSERIAL PRIMARY KEY,
    occurred_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT now(),
    url              VARCHAR(512),
    user_agent       VARCHAR(512),
    client_ip_masked VARCHAR(64),
    event_type       VARCHAR(50),
    response_session_id UUID
);

