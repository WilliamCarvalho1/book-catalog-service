CREATE TABLE IF NOT EXISTS book (
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255)      NOT NULL,
    author   VARCHAR(255)      NOT NULL,
    category VARCHAR(255)      NOT NULL,
    price    NUMERIC(19, 2)    NOT NULL,
    quantity INTEGER           NOT NULL
    );
);
CREATE INDEX IF NOT EXISTS idx_code ON book (id);