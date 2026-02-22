CREATE TABLE shopping_cart
(
    user_id VARCHAR(255) PRIMARY KEY
);

CREATE TABLE cart_item
(
    id         BIGSERIAL      PRIMARY KEY,
    user_id    VARCHAR(255)   NOT NULL REFERENCES shopping_cart (user_id) ON DELETE CASCADE,
    book_id    BIGINT         NOT NULL,
    title      VARCHAR(255)   NOT NULL,
    unit_price NUMERIC(19, 2) NOT NULL,
    quantity   INT            NOT NULL
);

CREATE INDEX idx_cart_item_user_id ON cart_item (user_id);
CREATE INDEX idx_cart_item_user_book ON cart_item (user_id, book_id);