package com.studies.bookcatalog.domain.model;

import com.studies.bookcatalog.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookTest {

    private Book createValidBook() {
        int currentYear = Year.now().getValue();
        return new Book(1L, "Title", "Author", "Category", BigDecimal.TEN, currentYear, 10);
    }

    @Test
    @DisplayName("constructor should create book with valid data")
    void constructorValid() {
        int year = Year.now().getValue();
        Book book = new Book(1L, "Title", "Author", "Category", BigDecimal.ONE, year, 0);

        assertThat(book.getId()).isEqualTo(1L);
        assertThat(book.getTitle()).isEqualTo("Title");
        assertThat(book.getAuthor()).isEqualTo("Author");
        assertThat(book.getCategory()).isEqualTo("Category");
        assertThat(book.getPrice()).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(book.getPublicationYear()).isEqualTo(year);
        assertThat(book.getQuantity()).isEqualTo(0);
    }

    @Nested
    @DisplayName("changeTitle")
    class ChangeTitle {
        @Test
        void shouldRejectBlankTitle() {
            assertThatThrownBy(() -> new Book(1L, " ", "Author", "Category", BigDecimal.TEN, Year.now().getValue(), 1))
                    .isInstanceOf(DomainException.class);
        }

        @Test
        void shouldTrimTitle() {
            int year = Year.now().getValue();
            Book book = new Book(1L, "  Title  ", "Author", "Category", BigDecimal.TEN, year, 1);
            assertThat(book.getTitle()).isEqualTo("Title");
        }
    }

    @Nested
    @DisplayName("changeAuthor")
    class ChangeAuthor {
        @Test
        void shouldRejectBlankAuthor() {
            int year = Year.now().getValue();

            assertThatThrownBy(() -> new Book(1L, "Title", " ", "Category", BigDecimal.TEN, year, 1))
                    .isInstanceOf(DomainException.class);
        }

        @Test
        void shouldTrimAuthor() {
            int year = Year.now().getValue();
            Book book = new Book(1L, "Title", "  Author  ", "Category", BigDecimal.TEN, year, 1);
            assertThat(book.getAuthor()).isEqualTo("Author");
        }
    }

    @Nested
    @DisplayName("changeCategory")
    class ChangeCategory {
        @Test
        void shouldRejectBlankCategory() {
            int year = Year.now().getValue();

            assertThatThrownBy(() -> new Book(1L, "Title", "Author", " ", BigDecimal.TEN, year, 1))
                    .isInstanceOf(DomainException.class);
        }

        @Test
        void shouldTrimCategory() {
            int year = Year.now().getValue();
            Book book = new Book(1L, "Title", "Author", "  Category  ", BigDecimal.TEN, year, 1);
            assertThat(book.getCategory()).isEqualTo("Category");
        }
    }

    @Nested
    @DisplayName("changePrice")
    class ChangePrice {
        @Test
        void shouldRejectNegativePrice() {
            int year = Year.now().getValue();

            assertThatThrownBy(() -> new Book(1L, "Title", "Author", "Category", new BigDecimal(-1), year, 1))
                    .isInstanceOf(DomainException.class);
        }

        @Test
        void shouldAcceptPositivePrice() {
            int year = Year.now().getValue();
            Book book = new Book(1L, "Title", "Author", "Category", BigDecimal.ONE, year, 1);
            assertThat(book.getPrice()).isEqualByComparingTo(BigDecimal.ONE);
        }
    }

    @Nested
    @DisplayName("changeQuantity")
    class ChangeQuantity {
        @Test
        void shouldRejectNegativeQuantity() {
            Book book = createValidBook();

            BookUpdate update = new BookUpdate(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(-1)
            );

            assertThatThrownBy(() -> book.applyUpdate(update))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Quantity cannot be negative");
        }

        @Test
        void shouldAllowZeroOrPositiveQuantity() {
            Book book = createValidBook();

            book.applyUpdate(new BookUpdate(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(0)
            ));
            assertThat(book.getQuantity()).isZero();

            book.applyUpdate(new BookUpdate(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(5)
            ));
            assertThat(book.getQuantity()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("changePublicationYear")
    class ChangePublicationYear {
        @Test
        void shouldRejectOutOfRangeYear() {
            Book book = createValidBook();

            assertThatThrownBy(() -> book.applyUpdate(new BookUpdate(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(1700),
                    Optional.empty()
            )))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Invalid publication year");

            int futureYear = Year.now().getValue() + 1;
            assertThatThrownBy(() -> book.applyUpdate(new BookUpdate(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(futureYear),
                    Optional.empty()
            )))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Invalid publication year");
        }

        @Test
        void shouldAcceptValidYear() {
            Book book = createValidBook();
            int currentYear = Year.now().getValue();

            book.applyUpdate(new BookUpdate(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(currentYear),
                    Optional.empty()
            ));
            assertThat(book.getPublicationYear()).isEqualTo(currentYear);
        }
    }

    @Nested
    @DisplayName("applyUpdate")
    class ApplyUpdate {
        @Test
        void shouldUpdateMultipleFields() {
            Book book = createValidBook();
            int currentYear = Year.now().getValue();

            BookUpdate update = new BookUpdate(
                    Optional.of("New Title"),
                    Optional.of("New Author"),
                    Optional.of("New Category"),
                    Optional.of(BigDecimal.valueOf(20)),
                    Optional.of(currentYear),
                    Optional.of(50)
            );

            book.applyUpdate(update);

            assertThat(book.getTitle()).isEqualTo("New Title");
            assertThat(book.getAuthor()).isEqualTo("New Author");
            assertThat(book.getCategory()).isEqualTo("New Category");
            assertThat(book.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(20));
            assertThat(book.getPublicationYear()).isEqualTo(currentYear);
            assertThat(book.getQuantity()).isEqualTo(50);
        }

        @Test
        void shouldIgnoreEmptyOptionals() {
            Book book = createValidBook();
            BigDecimal originalPrice = book.getPrice();
            Integer originalYear = book.getPublicationYear();
            Integer originalQuantity = book.getQuantity();

            BookUpdate update = new BookUpdate(
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty()
            );

            book.applyUpdate(update);

            assertThat(book.getPrice()).isEqualByComparingTo(originalPrice);
            assertThat(book.getPublicationYear()).isEqualTo(originalYear);
            assertThat(book.getQuantity()).isEqualTo(originalQuantity);
        }
    }
}
