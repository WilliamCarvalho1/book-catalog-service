CREATE TABLE IF NOT EXISTS shopping_cart (
    user_id   VARCHAR(255) PRIMARY KEY,
    cart_json TEXT NOT NULL
);