package com.studies.bookstore.adapter.in.controller;

import com.studies.bookstore.adapter.in.controller.dto.CartItemRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.CartItemUpdateRequestDTO;
import com.studies.bookstore.adapter.in.controller.dto.ShoppingCartResponseDTO;
import com.studies.bookstore.adapter.in.controller.mapper.ShoppingCartWebMapper;
import com.studies.bookstore.application.port.in.*;
import com.studies.bookstore.domain.model.ShoppingCart;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
    private final ExportCartUseCase exportCartUseCase;

    public ShoppingCartController(AddItemToCartUseCase addItemToCartUseCase,
                                  UpdateCartItemQuantityUseCase updateCartItemQuantityUseCase,
                                  RemoveCartItemUseCase removeCartItemUseCase,
                                  GetCartUseCase getCartUseCase,
                                  ClearCartUseCase clearCartUseCase,
                                  ExportCartUseCase exportCartUseCase) {
        this.addItemToCartUseCase = addItemToCartUseCase;
        this.updateCartItemQuantityUseCase = updateCartItemQuantityUseCase;
        this.removeCartItemUseCase = removeCartItemUseCase;
        this.getCartUseCase = getCartUseCase;
        this.clearCartUseCase = clearCartUseCase;
        this.exportCartUseCase = exportCartUseCase;
    }

    @GetMapping
    public ResponseEntity<EntityModel<ShoppingCartResponseDTO>> getCart() {
        String userId = getCurrentUserId();
        ShoppingCart cart = getCartUseCase.getCart(userId);
        ShoppingCartResponseDTO dto = ShoppingCartWebMapper.toResponseDTO(cart);
        return ResponseEntity.status(HttpStatus.OK).body(toModel(dto));
    }

    @PostMapping("/items")
    public ResponseEntity<EntityModel<ShoppingCartResponseDTO>> addItem(@Valid @RequestBody CartItemRequestDTO request) {
        String userId = getCurrentUserId();
        ShoppingCart cart = addItemToCartUseCase.addItem(userId, request.bookId(), request.quantity());
        ShoppingCartResponseDTO dto = ShoppingCartWebMapper.toResponseDTO(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body(toModel(dto));
    }

    @PutMapping("/items/{bookId}")
    public ResponseEntity<EntityModel<ShoppingCartResponseDTO>> updateItem(@NotNull @PathVariable Long bookId,
                                                                           @Valid @RequestBody CartItemUpdateRequestDTO request) {
        String userId = getCurrentUserId();
        ShoppingCart cart = updateCartItemQuantityUseCase.updateItemQuantity(userId, bookId, request.quantity());
        ShoppingCartResponseDTO dto = ShoppingCartWebMapper.toResponseDTO(cart);
        return ResponseEntity.status(HttpStatus.OK).body(toModel(dto));
    }

    @DeleteMapping("/items/{bookId}")
    public ResponseEntity<ShoppingCartResponseDTO> removeItem(@NotNull @PathVariable Long bookId) {
        String userId = getCurrentUserId();
        ShoppingCart cart = removeCartItemUseCase.removeItem(userId, bookId);
        return ResponseEntity.status(HttpStatus.OK).body(ShoppingCartWebMapper.toResponseDTO(cart));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart() {
        String userId = getCurrentUserId();
        clearCartUseCase.clearCart(userId);
    }

    @PostMapping("/export")
    public ResponseEntity<CartExportResponse> exportCart() {
        String userId = getCurrentUserId();
        String filePath = exportCartUseCase.exportCartForUser(userId);
        CartExportResponse body = new CartExportResponse("Cart exported successfully", filePath);
        return ResponseEntity.status(HttpStatus.CREATED).body(body);
    }

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private EntityModel<ShoppingCartResponseDTO> toModel(ShoppingCartResponseDTO dto) {
        EntityModel<ShoppingCartResponseDTO> model = EntityModel.of(dto);

        // Self link to view cart
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ShoppingCartController.class).getCart()).withSelfRel());

        // Link to add items
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ShoppingCartController.class)
                .addItem(null)).withRel("add-item"));

        // Link to export cart
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ShoppingCartController.class)
                .exportCart()).withRel("export"));

        return model;
    }

    public record CartExportResponse(String message, String filePath) {}
}