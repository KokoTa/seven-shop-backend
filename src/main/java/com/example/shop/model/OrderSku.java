package com.example.shop.model;

import com.example.shop.dto.SkuInfoDTO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

/**
 * 对应 Order 的 snapItems
 */
@Getter
@Setter
public class OrderSku {
    private Long id;
    private Long spuId;
    private BigDecimal finalPrice; // sku * count
    private BigDecimal singlePrice; // sku 单价
    private List<String> specValues; // 保留 sku 规格值
    private Long count;
    private String img;
    private String title;

    public OrderSku(Sku sku, SkuInfoDTO skuInfoDTO) {
        this.id = sku.getId();
        this.spuId = sku.getSpuId();
        this.singlePrice = sku.getActualPrice();
        this.finalPrice = sku.getActualPrice().multiply(new BigDecimal(skuInfoDTO.getCount()));
        this.count = skuInfoDTO.getCount();
        this.img = sku.getImg();
        this.title = sku.getTitle();
        this.specValues = sku.getSpecValueList();
    }
}
