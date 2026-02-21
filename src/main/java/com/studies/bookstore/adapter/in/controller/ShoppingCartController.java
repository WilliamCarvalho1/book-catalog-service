package com.studies.bookstore.adapter.in.controller;

import com.studies.bookstore.adapter.in.controller.dto.CartItemRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.CartItemUpdateRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.ShoppingCartResponseDTO;
import com.studies.bookstore.adapter.in.controller.mapper.ShoppingCartWebMapper;
import com.studies.bookstore.application.port.in.*;
import com.studies.bookstore.application.service.ShoppingCartExportService;
import com.studies.bookstore.domain.model.ShoppingCart;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@SecurityRequirement(name = "bearerAuth")
public class ShoppingCartController {

    private final AddItemToCartUseCase addItemToCartUseCase;
    private final UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase;
    private final RemoveCartItemUseCase removeCartItemUseCase;
    private final GetCartUseCase getCartUseCase;
    private final ClearCartUseCase clearCartUseCase;
    private final ShoppingCartExportService exportService;
    private final ExportCartUseCase exportCartUseCase;

    public ShoppingCartController(AddItemToCartUseCase addItemToCartUseCase,
                                  UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase,
                                  RemoveCartItemUseCase removeCartItemUseCase,
                                  GetCartUseCase getCartUseCase,
                                  ClearCartUseCase clearCartUseCase,
                                  ShoppingCartExportService exportService,
                                  ExportCartUseCase exportCartUseCase) {
        this.addItemToCartUseCase = addItemToCartUseCase;
        this.updateCartItemQuantityUseCase = updateCartItemQuantityUseCase;
        this.removeCartItemUseCase = removeCartItemUseCase;
        this.getCartUseCase = getCartUseCase;
        this.clearCartUseCase = clearCartUseCase;
        this.exportService = exportService;
        this.exportCartUseCase = exportCartUseCase;
    }

    @GetMapping
    public ResponseEntity<ShoppingCartResponseDTO> getCart() {
        String userId = getCurrentUserId();
        ShoppingCart cart = getCartUseCase.getCart(userId);
        return ResponseEntity.ok(ShoppingCartWebMapper.toResponseDTO(cart));
    }

    @PostMapping("/items")
    public ResponseEntity<ShoppingCartResponseDTO> addItem(@Valid @RequestBody CartItemRequestDTO request) {
        String userId = getCurrentUserId();
        ShoppingCart cart = addItemToCartUseCase.addItem(userId, request.bookId(), request.quantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ShoppingCartWebMapper.toResponseDTO(cart));
    }

    @PutMapping("/items/{bookId}")
    public ResponseEntity<ShoppingCartResponseDTO> updateItem(@NotNull @PathVariable Long bookId,
                                                              @Valid @RequestBody CartItemUpdateRequestDTO request) {
        String userId = getCurrentUserId();
        ShoppingCart cart = updateCartItemQuantityUseCase.updateItemQuantity(userId, bookId, request.quantity());
        return ResponseEntity.ok(ShoppingCartWebMapper.toResponseDTO(cart));
    }

    @DeleteMapping("/items/{bookId}")
    public ResponseEntity<ShoppingCartResponseDTO> removeItem(@NotNull @PathVariable Long bookId) {
        String userId = getCurrentUserId();
        ShoppingCart cart = removeCartItemUseCase.removeItem(userId, bookId);
        return ResponseEntity.ok(ShoppingCartWebMapper.toResponseDTO(cart));
    }

    @DeleteMapping
    public ResponseEntity<Void> clearCart() {
        String userId = getCurrentUserId();
        clearCartUseCase.clearCart(userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/export")
    public ResponseEntity<CartExportResponse> exportCart() {
        String userId = getCurrentUserId();
        ShoppingCart cart = getCartUseCase.getCart(userId);
        String filePath = exportCartUseCase.exportCart(cart);
        CartExportResponse body = new CartExportResponse("Cart exported successfully", filePath);
        return ResponseEntity.ok(body);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    public record CartExportResponse(String message, String filePath) {}
}