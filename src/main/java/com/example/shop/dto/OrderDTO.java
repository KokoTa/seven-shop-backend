package com.example.shop.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class OrderDTO {
    @DecimalMin(value = "0.00", message = "非法数值")
    @DecimalMax(value = "99999999.99", message = "非法数值")
    private BigDecimal totalPrice; // 原价
    private BigDecimal finalTotalPrice; // 最终价
    private Long couponId; // 优惠券
    private List<SkuInfoDTO> skuInfoList; // sku快照
    private OrderAddressDTO address; // 地址快照
}
