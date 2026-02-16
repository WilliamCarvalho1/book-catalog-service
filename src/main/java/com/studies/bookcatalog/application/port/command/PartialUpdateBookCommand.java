package com.studies.bookcatalog.application.port.command;

import java.math.BigDecimal;
import java.util.Optional;

public record PartialUpdateBookCommand(
        Optional<BigDecimal> price,
        Optional<Integer> quantity
) {
}
