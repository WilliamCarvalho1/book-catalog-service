package com.studies.bookcatalog.application.port.command;

import java.math.BigDecimal;
import java.util.Optional;

public record UpdateBookCommand(
        Optional<String> title,
        Optional<String> author,
        Optional<String> category,
        Optional<BigDecimal> price,
        Optional<Integer> publicationYear,
        Optional<Integer> quantity
) {
}
