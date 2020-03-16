package com.example.shop.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
public class SpuDetailImg extends BaseEntity {
    @Id
    private Long id;
    private String img;
    private Integer spuId;
    private Long index;
}
