package com.studies.bookstore.application.service;

import com.studies.bookstore.application.exception.InvalidRequestException;
import com.studies.bookstore.application.exception.RequestNotFoundException;
import com.studies.bookstore.application.port.out.BookRepositoryPort;
import com.studies.bookstore.application.port.out.ShoppingCartRepositoryPort;
import com.studies.bookstore.domain.model.Book;
import com.studies.bookstore.domain.model.CartItem;
import com.studies.bookstore.domain.model.ShoppingCart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ShoppingCartServiceTest {

    private final ShoppingCartRepositoryPort cartRepository = Mockito.mock(ShoppingCartRepositoryPort.class);
    private final BookRepositoryPort bookRepository = Mockito.mock(BookRepositoryPort.class);
    private final ShoppingCartService service = new ShoppingCartService(cartRepository, bookRepository);

    @Nested
    @DisplayName("addItem")
    class AddItem {

        @Test
        @DisplayName("should throw InvalidRequestException when quantity <= 0")
        void shouldThrowWhenQuantityNotPositive() {
            assertThatThrownBy(() -> service.addItem("user1", 1L, 0))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage("Quantity must be greater than zero");
        }

        @Test
        @DisplayName("should throw RequestNotFoundException when book does not exist")
        void shouldThrowWhenBookNotFound() {
            when(bookRepository.findById(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.addItem("user1", 1L, 1))
                    .isInstanceOf(RequestNotFoundException.class);
        }

        @Test
        @DisplayName("should create new cart when none exists and save with new item")
        void shouldCreateNewCartAndSave() {
            Book book = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 5);
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.empty());
            when(cartRepository.save(any(ShoppingCart.class))).thenAnswer(inv -> inv.getArgument(0));

            ShoppingCart result = service.addItem("user1", 1L, 2);

            assertThat(result.getUserId()).isEqualTo("user1");
            assertThat(result.getItems()).hasSize(1);
            CartItem item = result.getItems().get(0);
            assertThat(item.getBookId()).isEqualTo(1L);
            assertThat(item.getQuantity()).isEqualTo(2);
            verify(cartRepository).save(result);
        }

        @Test
        @DisplayName("should wrap DataAccessException into InvalidRequestException when saving")
        void shouldWrapDataAccessExceptionOnSave() {
            Book book = new Book(1L, "Book 1", "Author", "Category", BigDecimal.TEN, 2020, 5);
            when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.of(new ShoppingCart("user1")));
            when(cartRepository.save(any(ShoppingCart.class))).thenThrow(new DataAccessException("db error") {
            });

            assertThatThrownBy(() -> service.addItem("user1", 1L, 1))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("Database error:");
        }
    }

    @Nested
    @DisplayName("updateItemQuantity")
    class UpdateItemQuantity {

        @Test
        @DisplayName("should throw InvalidRequestException when quantity is negative")
        void shouldThrowWhenQuantityNegative() {
            assertThatThrownBy(() -> service.updateItemQuantity("user1", 1L, -1))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessage("Quantity cannot be negative");
        }

        @Test
        @DisplayName("should throw RequestNotFoundException when cart does not exist")
        void shouldThrowWhenCartNotFound() {
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.updateItemQuantity("user1", 1L, 1))
                    .isInstanceOf(RequestNotFoundException.class);
        }

        @Test
        @DisplayName("should update quantity and save cart")
        void shouldUpdateQuantityAndSave() {
            ShoppingCart cart = new ShoppingCart("user1");
            cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 1));
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.of(cart));
            when(cartRepository.save(any(ShoppingCart.class))).thenAnswer(inv -> inv.getArgument(0));

            ShoppingCart result = service.updateItemQuantity("user1", 1L, 3);

            assertThat(result.getItems()).hasSize(1);
            assertThat(result.getItems().get(0).getQuantity()).isEqualTo(3);
            verify(cartRepository).save(cart);
        }

        @Test
        @DisplayName("should wrap DataAccessException into InvalidRequestException on update")
        void shouldWrapDataAccessExceptionOnUpdate() {
            ShoppingCart cart = new ShoppingCart("user1");
            cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 1));
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.of(cart));
            when(cartRepository.save(any(ShoppingCart.class))).thenThrow(new DataAccessException("db error") {
            });

            assertThatThrownBy(() -> service.updateItemQuantity("user1", 1L, 3))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("Database error:");
        }
    }

    @Nested
    @DisplayName("removeItem")
    class RemoveItem {

        @Test
        @DisplayName("should throw RequestNotFoundException when cart does not exist")
        void shouldThrowWhenCartNotFound() {
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.removeItem("user1", 1L))
                    .isInstanceOf(RequestNotFoundException.class);
        }

        @Test
        @DisplayName("should remove item and save cart")
        void shouldRemoveItemAndSave() {
            ShoppingCart cart = new ShoppingCart("user1");
            cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 1));
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.of(cart));
            when(cartRepository.save(any(ShoppingCart.class))).thenAnswer(inv -> inv.getArgument(0));

            ShoppingCart result = service.removeItem("user1", 1L);

            assertThat(result.getItems()).isEmpty();
            verify(cartRepository).save(cart);
        }

        @Test
        @DisplayName("should wrap DataAccessException into InvalidRequestException on remove")
        void shouldWrapDataAccessExceptionOnRemove() {
            ShoppingCart cart = new ShoppingCart("user1");
            cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 1));
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.of(cart));
            when(cartRepository.save(any(ShoppingCart.class))).thenThrow(new DataAccessException("db error") {
            });

            assertThatThrownBy(() -> service.removeItem("user1", 1L))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("Database error:");
        }
    }

    @Nested
    @DisplayName("getCart")
    class GetCart {

        @Test
        @DisplayName("should return existing cart when found")
        void shouldReturnExistingCart() {
            ShoppingCart existing = new ShoppingCart("user1");
            existing.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 1));
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.of(existing));

            ShoppingCart result = service.getCart("user1");

            assertThat(result).isSameAs(existing);
        }

        @Test
        @DisplayName("should create new empty cart when none exists")
        void shouldCreateNewCartWhenNoneExists() {
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.empty());

            ShoppingCart result = service.getCart("user1");

            assertThat(result.getUserId()).isEqualTo("user1");
            assertThat(result.getItems()).isEmpty();
        }
    }

    @Nested
    @DisplayName("clearCart")
    class ClearCart {

        @Test
        @DisplayName("should clear existing cart and save it")
        void shouldClearExistingCartAndSave() {
            ShoppingCart cart = new ShoppingCart("user1");
            cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 1));
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.of(cart));

            service.clearCart("user1");

            assertThat(cart.getItems()).isEmpty();
            verify(cartRepository).save(cart);
        }

        @Test
        @DisplayName("should create new empty cart when none exists and save it")
        void shouldCreateNewEmptyCartWhenNoneExists() {
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.empty());

            service.clearCart("user1");

            ArgumentCaptor<ShoppingCart> captor = ArgumentCaptor.forClass(ShoppingCart.class);
            verify(cartRepository).save(captor.capture());
            ShoppingCart saved = captor.getValue();

            assertThat(saved.getUserId()).isEqualTo("user1");
            assertThat(saved.getItems()).isEmpty();
        }

        @Test
        @DisplayName("should wrap DataAccessException into InvalidRequestException on clear")
        void shouldWrapDataAccessExceptionOnClear() {
            ShoppingCart cart = new ShoppingCart("user1");
            when(cartRepository.findByUserId("user1")).thenReturn(Optional.of(cart));
            when(cartRepository.save(any(ShoppingCart.class))).thenThrow(new DataAccessException("db error") {
            });

            assertThatThrownBy(() -> service.clearCart("user1"))
                    .isInstanceOf(InvalidRequestException.class)
                    .hasMessageContaining("Database error:");
        }
    }
}