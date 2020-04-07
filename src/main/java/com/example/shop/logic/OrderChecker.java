package com.example.shop.logic;

import com.example.shop.bo.SkuOrderBO;
import com.example.shop.dto.OrderDTO;
import com.example.shop.dto.SkuInfoDTO;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.OrderSku;
import com.example.shop.model.Sku;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单校验
 * 1. 检查前端传入的 SKU 在售状态和服务端是否一致
 * 2. 检查 SKU 是否售罄、是否超出库存、是否超出限额
 * 3. 验证前端传过来的原始总价格(未使用优惠券时的总价)和后端的原始总价格是否一致
 * 4. 优惠券校验
 * 5. 返回订单快照信息
 */
public class OrderChecker {

    private OrderDTO orderDTO;
    private List<Sku> serverSkuList;
    private CouponChecker couponChecker;
    private Integer maxSkuLimit;

    @Getter
    private List<OrderSku> orderSkuList = new ArrayList<>();

    public OrderChecker(OrderDTO orderDTO, List<Sku> serverSkuList, CouponChecker couponChecker, Integer maxSkuLimit) {
        this.orderDTO = orderDTO;
        this.serverSkuList = serverSkuList;
        this.couponChecker = couponChecker;
        this.maxSkuLimit = maxSkuLimit;
    }

    public void isOK() {
        BigDecimal serverTotalPrice = new BigDecimal("0");
        List<SkuOrderBO> skuOrderBOList = new ArrayList<>();

        // 前后端商品在售状态是否一致
        this.skuSaleStatusCheck(orderDTO.getSkuInfoList().size(), serverSkuList.size());

        for (int i = 0; i < serverSkuList.size(); i++) {
            Sku sku = serverSkuList.get(i);
            SkuInfoDTO skuInfoDTO = orderDTO.getSkuInfoList().get(i);

            this.skuZeroStockCheck(sku); // 是否售空
            this.skuBeyondStockCheck(sku, skuInfoDTO); // 是否超出库存
            this.skuBeyondLimitCheck(skuInfoDTO); // 是否超出限额

            serverTotalPrice = serverTotalPrice.add(calculateSkuPrice(sku, skuInfoDTO)); // 计算总价
            skuOrderBOList.add(new SkuOrderBO(sku, skuInfoDTO)); // 在优惠券校验中使用
            orderSkuList.add(new OrderSku(sku, skuInfoDTO)); // SKU 快照
        }

        // 前后端总价是否一致
        this.skuTotalPriceCheck(orderDTO.getTotalPrice(), serverTotalPrice);

        // 优惠券校验
        if (couponChecker != null) {
            couponChecker.timeRangeOK(); // 优惠券是否过期
            couponChecker.finalTotalPriceOK(orderDTO.getFinalTotalPrice(), serverTotalPrice); // 使用优惠券后，前后端总价是否一致
            couponChecker.canBeUsed(skuOrderBOList); // 优惠券是否到达门槛
        }
    }

    public String getSnapImg() {
        return serverSkuList.get(0).getImg();
    }

    public String getSnapTitle() {
        return serverSkuList.get(0).getTitle();
    }

    public Long getTotalCount() {
        return orderDTO.getSkuInfoList()
                .stream()
                .map(SkuInfoDTO::getCount)
                .reduce(Long::sum)
                .orElse((long) 0);

    }

    private void skuSaleStatusCheck(int count1, int count2) {
        if (count1 != count2) {
            throw new ParameterException(50002);
        }
    }

    private void skuZeroStockCheck(Sku sku) {
        if (sku.getStock() == 0) {
            throw new ParameterException(50001);
        }
    }

    private void skuBeyondStockCheck(Sku sku, SkuInfoDTO skuInfoDTO) {
        if (sku.getStock() < skuInfoDTO.getCount()) {
            throw new ParameterException(50003);
        }
    }

    private void skuBeyondLimitCheck(SkuInfoDTO skuInfoDTO) {
        if (skuInfoDTO.getCount() > maxSkuLimit) {
            throw new ParameterException(50004);
        }
    }

    private BigDecimal calculateSkuPrice(Sku sku, SkuInfoDTO skuInfoDTO) {
        if (skuInfoDTO.getCount() <= 0) throw new ParameterException(50007);
        return sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
    }

    private void skuTotalPriceCheck(BigDecimal frontTotalPrice, BigDecimal serverTotalPrice) {
        if (frontTotalPrice.compareTo(serverTotalPrice) != 0) throw new ParameterException(50005);
    }
}
