package com.example.shop.core.money;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 银行家模式，四舍六入
 */
@Component
public class HalfEvenRound implements IMoney {
    @Override
    public BigDecimal dicount(BigDecimal original, BigDecimal discount) {
        return original.multiply(discount).setScale(2, RoundingMode.HALF_EVEN);
    }
}
