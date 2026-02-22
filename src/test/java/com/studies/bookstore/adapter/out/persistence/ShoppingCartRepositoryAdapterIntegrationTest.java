package com.studies.bookstore.adapter.out.persistence;

import com.studies.bookstore.AbstractPostgresContainerTest;
import com.studies.bookstore.adapter.out.persistence.entity.CartItemEntity;
import com.studies.bookstore.adapter.out.persistence.entity.ShoppingCartEntity;
import com.studies.bookstore.application.port.out.ShoppingCartRepositoryPort;
import com.studies.bookstore.domain.model.CartItem;
import com.studies.bookstore.domain.model.ShoppingCart;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(ShoppingCartRepositoryAdapter.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryAdapterIntegrationTest extends AbstractPostgresContainerTest {

    @Autowired
    private JpaShoppingCartRepository jpaRepository;

    @Autowired
    private ShoppingCartRepositoryPort cartRepository;

    @Test
    @DisplayName("should persist and load shopping cart with items using real JPA + Flyway schema")
    void shouldPersistAndLoadCart() {
        // given
        String userId = "user-it";
        ShoppingCart cart = new ShoppingCart(userId);
        cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 2));
        cart.addItem(new CartItem(2L, "Book 2", BigDecimal.ONE, 3));

        // when
        cartRepository.save(cart);

        // then - verify persisted state via JPA entity
        ShoppingCartEntity entity = jpaRepository.findById(userId).orElseThrow();
        List<CartItemEntity> items = entity.getItems();
        assertThat(items).hasSize(2);
        assertThat(items).anySatisfy(e -> {
            assertThat(e.getBookId()).isEqualTo(1L);
            assertThat(e.getTitle()).isEqualTo("Book 1");
            assertThat(e.getUnitPrice()).isEqualByComparingTo(BigDecimal.TEN);
            assertThat(e.getQuantity()).isEqualTo(2);
        });

        // and when we load back through the port, we get an equivalent aggregate
        ShoppingCart loaded = cartRepository.findByUserId(userId).orElseThrow();
        assertThat(loaded.getUserId()).isEqualTo(userId);
        assertThat(loaded.getItems()).hasSize(2);
        assertThat(loaded.getTotalQuantity()).isEqualTo(5);
        assertThat(loaded.getTotalPrice()).isEqualByComparingTo(new BigDecimal("23"));
    }

    @Test
    @DisplayName("should replace old cart items when saving updated cart")
    void shouldReplaceItemsOnUpdate() {
        String userId = "user-it-update";
        // initial cart with one item
        ShoppingCart cart = new ShoppingCart(userId);
        cart.addItem(new CartItem(1L, "Book 1", BigDecimal.TEN, 1));
        cartRepository.save(cart);

        // verify initial state
        ShoppingCartEntity initialEntity = jpaRepository.findById(userId).orElseThrow();
        assertThat(initialEntity.getItems()).hasSize(1);

        // updated cart with a different item set
        ShoppingCart updatedCart = new ShoppingCart(userId);
        updatedCart.addItem(new CartItem(2L, "Book 2", BigDecimal.ONE, 4));

        cartRepository.save(updatedCart);

        // now DB should only contain the new item
        ShoppingCartEntity entity = jpaRepository.findById(userId).orElseThrow();
        List<CartItemEntity> items = entity.getItems();
        assertThat(items).hasSize(1);
        CartItemEntity item = items.get(0);
        assertThat(item.getBookId()).isEqualTo(2L);
        assertThat(item.getTitle()).isEqualTo("Book 2");
        assertThat(item.getQuantity()).isEqualTo(4);

        // and loading via port should reflect the updated state
        ShoppingCart loaded = cartRepository.findByUserId(userId).orElseThrow();
        assertThat(loaded.getItems()).hasSize(1);
        assertThat(loaded.getItems().get(0).getBookId()).isEqualTo(2L);
        assertThat(loaded.getItems().get(0).getQuantity()).isEqualTo(4);
    }
}