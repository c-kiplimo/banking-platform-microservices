CREATE TABLE IF NOT EXISTS customer
(
    record_id    VARCHAR(50)  NOT NULL PRIMARY KEY,
    first_name         VARCHAR(100) NOT NULL,
    last_name VARCHAR(255)  NOT NULL,
    other_name     VARCHAR(255)  NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NULL
)