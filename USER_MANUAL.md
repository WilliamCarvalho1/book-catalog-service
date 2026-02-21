# Book Store Shopping Cart - User Manual

## 1. Introduction

This document explains how to run the Book Store API and how to use the new Shopping Cart features.

The shopping cart lets each authenticated user add books, change quantities, remove items, and view a summary with per-item cost and total price.

## 2. How to Start the Application

### 2.1. Prerequisites

- Java 17 installed
- Maven installed
- Docker and Docker Compose installed (for PostgreSQL)

### 2.2. Start PostgreSQL

```bash
cd book-store
docker-compose up -d
```

### 2.3. Build the Application

```bash
mvn clean package
```

### 2.4. Run the Application

You can run with Maven:

```bash
mvn spring-boot:run
```

Or run the built JAR:

```bash
java -jar target/book-store-0.0.1-SNAPSHOT.jar
```

The API will be available at `http://localhost:8080` and Swagger UI at `http://localhost:8080/swagger-ui.html`.

## 3. Authentication

1. Call the login endpoint `/api/auth/login` with valid credentials (see project README for details).
2. Copy the returned JWT token.
3. In your API client (Swagger UI, Postman, Insomnia, etc.), configure an `Authorization` header:
   - Key: `Authorization`
   - Value: `Bearer <your-token>`

All shopping cart endpoints require a valid bearer token. The cart is stored per user, identified by the authenticated username from the token.

## 4. Shopping Cart Features

Base URL for cart operations:

`/api/v1/cart`

### 4.1. View Your Cart

**Endpoint:** `GET /api/v1/cart`

**Description:** Returns the current contents of your shopping cart, including each book, quantity, per-item subtotal, and cart totals.

**Response example:**

```json
{
  "userId": "john.doe",
  "items": [
    {
      "bookId": 1,
      "title": "Clean Code",
      "unitPrice": 50.0,
      "quantity": 2,
      "subtotal": 100.0
    }
  ],
  "total": 100.0,
  "itemCount": 1,
  "totalQuantity": 2
}
```

If your cart is empty, you'll get an empty `items` list and `total` equal to `0.0`.

### 4.2. Add an Item to the Cart

**Endpoint:** `POST /api/v1/cart/items`

**Request body:**

```json
{
  "bookId": 1,
  "quantity": 2
}
```

- `bookId`: ID of an existing book in the catalog.
- `quantity`: Must be a positive integer.

**Behavior:**
- If the book is not yet in your cart, it is added with the given quantity.
- If the book is already in your cart, the quantity is increased (no duplicate items).

The response returns the entire updated cart (same shape as `GET /api/v1/cart`).

### 4.3. Update Quantity of an Item

**Endpoint:** `PUT /api/v1/cart/items/{bookId}`

**Path parameter:**
- `bookId`: ID of the book whose quantity you want to change.

**Request body:**

```json
{
  "quantity": 3
}
```

Only the `quantity` field is used.

**Behavior:**
- If `quantity` is greater than 0, the item's quantity is updated.
- If `quantity` is 0, the item is removed from the cart.

The response returns the updated cart.

### 4.4. Remove an Item from the Cart

**Endpoint:** `DELETE /api/v1/cart/items/{bookId}`

**Path parameter:**
- `bookId`: ID of the book to remove.

**Behavior:**
- Removes the item from your cart if it exists.

The response returns the updated cart after removal.

### 4.5. Clear the Entire Cart

**Endpoint:** `DELETE /api/v1/cart`

**Behavior:**
- Removes all items from your cart.

The response is `204 No Content` when successful.

## 5. Data Persistence (JSON Storage)

Shopping cart data is persisted in PostgreSQL in a `shopping_cart` table. Each row stores the full cart for a user as JSON.

This satisfies the requirement to keep cart data as JSON, while using a database instead of a local path such as `c:\\temp`, which is not suitable for macOS.

In addition, the application can export the current shopping cart to a JSON file on disk to satisfy the requirement of generating a JSON file in a configurable directory.

## 6. Exporting the Cart to a JSON File

### 6.1. Export Endpoint

**Endpoint:** `POST /api/v1/cart/export`

**Description:**

Exports the current authenticated user's shopping cart to a JSON file on the server. The response includes a message and the absolute path of the generated file.

**Sample response:**

```json
{
  "message": "Cart exported successfully",
  "filePath": "/Users/john.doe/bookstore/cart-exports/shopping-cart-john.doe.json"
}
```

### 6.2. Export Directory Configuration

The export directory is configurable via the `cart.export.directory` property in `application.yml`:

```yaml
cart:
  export:
    directory: ${CART_EXPORT_DIR:${user.home}/bookstore/cart-exports}
```

This means:

- If the `CART_EXPORT_DIR` environment variable is defined, its value is used as the export directory.
- Otherwise, the default directory is `${user.home}/bookstore/cart-exports`.

### 6.3. Windows: Using `C:\\temp` as Required

The original requirement specifies that the JSON file must be saved under `c:\\temp`.

On Windows, you can fulfill this requirement by configuring the export directory as follows:

- Via environment variable (Command Prompt):

```bat
set CART_EXPORT_DIR=C:\\temp
```

- Or via `application.yml`:

```yaml
cart:
  export:
    directory: C:\\temp
```

After this configuration, the export endpoint will write files like:

```text
C:\\temp\\shopping-cart-john.doe.json
```

which matches the required path on Windows.

### 6.4. macOS / Linux Default Directory

On macOS and Linux, if you do not set `CART_EXPORT_DIR`, the default export directory will be:

```text
${user.home}/bookstore/cart-exports
```

For example:

- macOS: `/Users/john.doe/bookstore/cart-exports/shopping-cart-john.doe.json`
- Linux: `/home/john.doe/bookstore/cart-exports/shopping-cart-john.doe.json`

You can of course override this by setting `CART_EXPORT_DIR` to any valid path.

### 6.5. Export Scenario

1. **Login** and obtain a JWT token as described in section 3.
2. **Add items to your cart** using `POST /api/v1/cart/items`.
3. **Export your cart** using `POST /api/v1/cart/export` with the `Authorization: Bearer <token>` header.
4. **Check the export directory** (e.g., `C:\\temp` on Windows or `${user.home}/bookstore/cart-exports` on macOS/Linux).
5. Open the generated `shopping-cart-<userId>.json` file in a text editor to inspect your cart contents as JSON.

## 7. Example User Scenario

1. **Login** as `john.doe` and get a JWT token.
2. **Browse books** using the existing book endpoints (e.g., `GET /api/v1/books`).
3. **Add a book to your cart** using `POST /api/v1/cart/items`:
   - Body: `{ "bookId": 1, "quantity": 1 }`.
4. **Add the same book again** with `{ "bookId": 1, "quantity": 2 }`.
   - The cart will now show a single line for this book with `quantity: 3`, demonstrating that duplicates are not created.
5. **View your cart** using `GET /api/v1/cart`:
   - Confirm that each item shows its own `subtotal` and that `total` is the sum of all subtotals.
6. **Update the quantity** of a book using `PUT /api/v1/cart/items/1` with `{ "bookId": 1, "quantity": 2 }`.
   - Quantity drops to `2` and the `total` is updated.
7. **Remove the item** using `DELETE /api/v1/cart/items/1`.
   - The cart becomes empty.
8. **Clear the cart** at any time with `DELETE /api/v1/cart` if you want to reset all items.

This flow demonstrates all shopping cart requirements and how they work together.

## 8. Error Handling and Validation

- If you try to add or update with `quantity <= 0`, the API responds with a `400 Bad Request` and a descriptive error message.
- If you refer to a non-existing `bookId`, the API responds with `404 Not Found`.
- If internal persistence errors occur, the API responds with an error message indicating a database issue.

These behaviors are consistent with the existing global error handling in the project.