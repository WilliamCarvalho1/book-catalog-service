package com.studies.bookcatalog.domain.model;

import java.math.BigDecimal;
import java.util.Optional;

public record BookUpdate(
        Optional<String> title,
        Optional<String> author,
        Optional<String> category,
        Optional<BigDecimal> price,
        Optional<Integer> publicationYear,
        Optional<Integer> quantity
) {
}
