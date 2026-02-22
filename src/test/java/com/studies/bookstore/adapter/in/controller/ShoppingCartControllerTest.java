package com.studies.bookstore.adapter.in.controller;

import com.studies.bookstore.adapter.in.controller.dto.CartItemRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.CartItemUpdateRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.ShoppingCartResponseDTO;
import com.studies.bookstore.application.port.in.*;
import com.studies.bookstore.domain.model.CartItem;
import com.studies.bookstore.domain.model.ShoppingCart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ShoppingCartControllerTest {

    private final AddItemToCartUseCase addItemToCartUseCase = mock(AddItemToCartUseCase.class);
    private final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase = mock(UpdateCartItemQuantityUseCase.class);
    private final RemoveCartItemUseCase removeCartItemUseCase = mock(RemoveCartItemUseCase.class);
    private final GetCartUseCase getCartUseCase = mock(GetCartUseCase.class);
    private final ClearCartUseCase clearCartUseCase = mock(ClearCartUseCase.class);
    private final ExportCartUseCase exportCartUseCase = mock(ExportCartUseCase.class);

    private final ShoppingCartController controller = new ShoppingCartController(
            addItemToCartUseCase,
            updateCartItemQuantityUseCase,
            removeCartItemUseCase,
            getCartUseCase,
            clearCartUseCase,
            exportCartUseCase
    );

    @BeforeEach
    void setUpSecurityContext() {
        SecurityContext context = Mockito.mock(SecurityContext.class);
        Authentication auth = new UsernamePasswordAuthenticationToken("user1", null, List.of());
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);
    }

    private ShoppingCart buildSampleCart() {
        ShoppingCart cart = new ShoppingCart("user1");
        cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 2));
        return cart;
    }

    @Test
    @DisplayName("getCart should return mapped ShoppingCartResponseDTO for current user with links")
    void getCartShouldReturnCartForCurrentUser() {
        ShoppingCart cart = buildSampleCart();
        when(getCartUseCase.getCart("user1")).thenReturn(cart);

        ResponseEntity<EntityModel<ShoppingCartResponseDTO>> response = controller.getCart();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EntityModel<ShoppingCartResponseDTO> model = response.getBody();
        assertThat(model).isNotNull();
        ShoppingCartResponseDTO body = model.getContent();
        assertThat(body).isNotNull();
        assertThat(body.userId()).isEqualTo("user1");
        assertThat(body.items()).hasSize(1);
        assertThat(body.total()).isEqualByComparingTo(cart.getTotalPrice());
        assertThat(model.getLinks()).isNotEmpty();
        verify(getCartUseCase).getCart("user1");
    }

    @Test
    @DisplayName("addItem should delegate to use case with current user and return created response with links")
    void addItemShouldDelegateAndReturnCreated() {
        CartItemRequestDTO request = new CartItemRequestDTO(1L, 2);
        ShoppingCart cart = buildSampleCart();
        when(addItemToCartUseCase.addItem(eq("user1"), eq(1L), eq(2))).thenReturn(cart);

        ResponseEntity<EntityModel<ShoppingCartResponseDTO>> response = controller.addItem(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        EntityModel<ShoppingCartResponseDTO> model = response.getBody();
        assertThat(model).isNotNull();
        ShoppingCartResponseDTO body = model.getContent();
        assertThat(body).isNotNull();
        assertThat(body.userId()).isEqualTo("user1");
        assertThat(body.items()).hasSize(1);
        verify(addItemToCartUseCase).addItem("user1", 1L, 2);
    }

    @Test
    @DisplayName("updateItem should delegate to use case with current user and return updated cart with links")
    void updateItemShouldDelegateAndReturnUpdatedCart() {
        CartItemUpdateRequestDTO request = new CartItemUpdateRequestDTO(3);
        ShoppingCart cart = buildSampleCart();
        when(updateCartItemQuantityUseCase.updateItemQuantity(eq("user1"), eq(1L), eq(3))).thenReturn(cart);

        ResponseEntity<EntityModel<ShoppingCartResponseDTO>> response = controller.updateItem(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        EntityModel<ShoppingCartResponseDTO> model = response.getBody();
        assertThat(model).isNotNull();
        ShoppingCartResponseDTO body = model.getContent();
        assertThat(body).isNotNull();
        assertThat(body.userId()).isEqualTo("user1");
        verify(updateCartItemQuantityUseCase).updateItemQuantity("user1", 1L, 3);
    }

    @Test
    @DisplayName("removeItem should delegate to use case with current user and return updated cart")
    void removeItemShouldDelegateAndReturnUpdatedCart() {
        ShoppingCart cart = buildSampleCart();
        when(removeCartItemUseCase.removeItem(eq("user1"), eq(1L))).thenReturn(cart);

        ResponseEntity<ShoppingCartResponseDTO> response = controller.removeItem(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        ShoppingCartResponseDTO body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.userId()).isEqualTo("user1");
        verify(removeCartItemUseCase).removeItem("user1", 1L);
    }

    @Test
    @DisplayName("clearCart should delegate to use case with current user and return 204")
    void clearCartShouldDelegateAndReturnNoContent() {
        controller.clearCart();

        verify(clearCartUseCase).clearCart("user1");
    }

    @Test
    @DisplayName("exportCart should export current cart to JSON file via use case and return file path")
    void exportCartShouldExportAndReturnFilePath() {
        when(exportCartUseCase.exportCartForUser("user1"))
                .thenReturn("/tmp/bookstore/cart-exports/shopping-cart-user1.json");

        ResponseEntity<ShoppingCartController.CartExportResponse> response = controller.exportCart();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        ShoppingCartController.CartExportResponse body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.message()).isEqualTo("Cart exported successfully");
        assertThat(body.filePath()).contains("shopping-cart-user1.json");
        verify(exportCartUseCase).exportCartForUser("user1");
    }
}

