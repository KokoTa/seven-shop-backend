package com.example.shop.model;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "delete_tim is null")
@Table(name = "`Order`") // 由于是关键字，所以需要加指明表名
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String orderNo;
    private Integer userId;
    private BigDecimal totalPrice;
    private Integer totalCount;
    private String snapImg;
    private String snapTitle;
    private String snapItems;
    private String snapAddress;
    private String prepayId;
    private BigDecimal finalTotalPrice;
    private Integer status;
}
