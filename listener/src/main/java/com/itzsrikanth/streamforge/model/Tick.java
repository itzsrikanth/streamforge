package com.itzsrikanth.streamforge.model;

import java.math.BigDecimal;
import java.time.Instant;

public record Tick(
        String symbol,
        BigDecimal price,
        BigDecimal qty,
        Instant eventTime,
        Instant ingestTime
) {
    public Tick {
        if (symbol == null || symbol.isBlank()) {
            throw new IllegalArgumentException("symbol required");
        }
        if (price == null || price.signum() <= 0) {
            throw new IllegalArgumentException("price must be > 0");
        }
        if (qty == null || qty.signum() < 0) {
            throw new IllegalArgumentException("qty must be >= 0");
        }
        if (eventTime == null || ingestTime == null) {
            throw new IllegalArgumentException("timestamps required");
        }
    }
}
