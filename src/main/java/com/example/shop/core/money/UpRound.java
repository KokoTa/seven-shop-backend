package com.example.shop.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 向上取整
 */
@Component
public class UpRound implements IMoney {
    @Override
    public BigDecimal dicount(BigDecimal original, BigDecimal discount) {
        return original.multiply(discount).setScale(2, RoundingMode.UP);
    }
}
