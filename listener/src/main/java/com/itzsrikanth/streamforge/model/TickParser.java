package com.itzsrikanth.streamforge.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

public final class TickParser {
    private TickParser() {
    }

    public static Optional<Tick> toTick(TradeWire wire) {
        if (wire == null) {
            return Optional.empty();
        }
        try {
            BigDecimal price = parseDecimal(wire.price);
            BigDecimal qty = parseDecimal(wire.quantity);
            if (price == null || price.signum() <= 0) {
                return Optional.empty();
            }
            if (qty == null || qty.signum() < 0) {
                return Optional.empty();
            }
            if (wire.symbol == null || wire.symbol.isBlank()) {
                return Optional.empty();
            }
            Instant eventTime = Instant.ofEpochMilli(wire.tradeTime); // owned choice: T vs E
            return Optional.of(new Tick(
                    wire.symbol,
                    price,
                    qty,
                    eventTime,
                    Instant.now()
            ));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    private static BigDecimal parseDecimal(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        return new BigDecimal(raw.trim());
    }
}