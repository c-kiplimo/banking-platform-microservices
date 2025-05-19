CREATE TABLE IF NOT EXISTS account (
   record_id   BIGINT       NOT NULL PRIMARY KEY,
   iban        VARCHAR(255) NOT NULL,
   bic_swift   VARCHAR(255) NOT NULL,
   customer_id BIGINT       NOT NULL,
    created_at   TIMESTAMP    NOT NULL,
    updated_at   TIMESTAMP    NULL
 );
