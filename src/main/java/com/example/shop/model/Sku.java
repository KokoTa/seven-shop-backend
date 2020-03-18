package com.example.shop.model;

import com.example.shop.util.ListJsonConverter;
import com.example.shop.util.MapJsonConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Sku extends BaseEntity {
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean online;
    private String img;
    private String title;
    private Long spuId;
    private String code;
    private Long stock;
    private Long categoryId;
    private Long rootCategoryId;

    @Convert(converter = MapJsonConverter.class)
    private Map<String, Object> test;

    @Convert(converter = ListJsonConverter.class)
    private List<Spec> specs;
}
