package com.example.shop.model;

import com.example.shop.util.ListJsonConverter;
import com.example.shop.util.MapJsonConverter;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
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
    private Long userId;
    private BigDecimal totalPrice;
    private Integer totalCount;
    private String snapImg;
    private String snapTitle;
    @SuppressWarnings("JpaAttributeTypeInspection")
    @Convert(converter = ListJsonConverter.class)
    private List<Object> snapItems;
    @SuppressWarnings("JpaAttributeTypeInspection")
    @Convert(converter = ListJsonConverter.class)
    private List<Object> snapAddress;
    private String prepayId;
    private BigDecimal finalTotalPrice;
    private Integer status;
}
