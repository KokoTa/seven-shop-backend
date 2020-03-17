package com.example.shop.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpuSimpleVo {
    private Long id;
    private String title;
    private String subtitle;
    private String price;
    private Integer sketchSpecId;
    private String img;
    private String discountPrice;
    private String description;
    private String tags;
    private String forThemeImg;
}
