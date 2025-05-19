CREATE TABLE IF NOT EXISTS card
(
    record_id    VARCHAR(50)  NOT NULL PRIMARY KEY,
    alias          VARCHAR(100) NOT NULL,
    card_type  VARCHAR(255)  NOT NULL,
    pan      VARCHAR(255)  NULL,
    cvv      VARCHAR(255)  NULL,
    account_id BIGINT NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NULL
)