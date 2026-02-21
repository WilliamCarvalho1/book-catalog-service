package com.studies.bookstore.application.service;

import com.studies.bookstore.application.exception.InvalidRequestException;
import com.studies.bookstore.application.exception.RequestNotFoundException;
import com.studies.bookstore.application.port.in.*;
import com.studies.bookstore.application.port.out.BookRepositoryPort;
import com.studies.bookstore.application.port.out.ShoppingCartRepositoryPort;
import com.studies.bookstore.domain.model.Book;
import com.studies.bookstore.domain.model.CartItem;
import com.studies.bookstore.domain.model.ShoppingCart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

public class ShoppingCartService implements AddItemToCartUseCase, UpdateCartItemQuantityUseCase,
        RemoveCartItemUseCase, GetCartUseCase, ClearCartUseCase {

    private static final Logger logger = LoggerFactory.getLogger(ShoppingCartService.class);

    private final ShoppingCartRepositoryPort cartRepository;
    private final BookRepositoryPort bookRepository;

    private static final String BD_ERROR_MSG = "Database error: ";

    public ShoppingCartService(ShoppingCartRepositoryPort cartRepository, BookRepositoryPort bookRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public ShoppingCart addItem(String userId, Long bookId, int quantity) {
        if (quantity <= 0) {
            throw new InvalidRequestException("Quantity must be greater than zero");
        }
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RequestNotFoundException(bookId));

        try {
            ShoppingCart cart = cartRepository.findByUserId(userId).orElseGet(() -> new ShoppingCart(userId));
            CartItem item = new CartItem(book.getId(), book.getTitle(), book.getPrice(), quantity);
            cart.addItem(item);
            return cartRepository.save(cart);
        } catch (DataAccessException ex) {
            logger.error("Error while saving cart for user '{}': {}", userId, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public ShoppingCart updateItemQuantity(String userId, Long bookId, int quantity) {
        if (quantity < 0) {
            throw new InvalidRequestException("Quantity cannot be negative");
        }
        try {
            ShoppingCart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RequestNotFoundException(bookId));
            cart.updateItemQuantity(bookId, quantity);
            return cartRepository.save(cart);
        } catch (DataAccessException ex) {
            logger.error("Error while updating cart for user '{}': {}", userId, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public ShoppingCart removeItem(String userId, Long bookId) {
        try {
            ShoppingCart cart = cartRepository.findByUserId(userId)
                    .orElseThrow(() -> new RequestNotFoundException(bookId));
            cart.removeItem(bookId);
            return cartRepository.save(cart);
        } catch (DataAccessException ex) {
            logger.error("Error while updating cart for user '{}': {}", userId, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public ShoppingCart getCart(String userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> new ShoppingCart(userId));
    }

    @Override
    public void clearCart(String userId) {
        try {
            ShoppingCart cart = cartRepository.findByUserId(userId)
                    .orElseGet(() -> new ShoppingCart(userId));
            cart.clear();
            cartRepository.save(cart);
        } catch (DataAccessException ex) {
            logger.error("Error while clearing cart for user '{}': {}", userId, ex.getMessage(), ex);
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

}