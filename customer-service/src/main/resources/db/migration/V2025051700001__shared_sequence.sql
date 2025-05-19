CREATE TABLE IF NOT EXISTS shared_sequence
(
    key        VARCHAR(255) PRIMARY KEY,
    value      BIGINT    NOT NULL,
    factor     INT       NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NULL

);
