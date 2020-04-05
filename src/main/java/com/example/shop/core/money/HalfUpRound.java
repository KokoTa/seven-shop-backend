package com.example.shop.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 四舍五入
 */
@Component
public class HalfUpRound implements IMoney {
    @Override
    public BigDecimal dicount(BigDecimal original, BigDecimal discount) {
        return original.multiply(discount).setScale(2, RoundingMode.HALF_UP);
    }
}
