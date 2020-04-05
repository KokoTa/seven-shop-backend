package com.example.shop.core.money;

import java.math.BigDecimal;

public interface IMoney {

    /**
     * 实现不同折扣算法的折扣方法
     *
     * @param original 原价
     * @param discount 折扣率
     * @return
     */
    BigDecimal dicount(BigDecimal original, BigDecimal discount);
}
