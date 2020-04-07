package com.example.shop.model;

import com.example.shop.dto.OrderAddressDTO;
import com.example.shop.util.GenericJsonConverter;
import com.example.shop.util.ListJsonConverter;
import com.example.shop.util.MapJsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
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
    private Long totalCount;
    private String snapImg;
    private String snapTitle;
    private String prepayId;
    private BigDecimal finalTotalPrice;
    private Integer status;

    private String snapItems;
    private String snapAddress;

    public List<OrderSku> getSnapItems() {
        if (this.snapItems == null) {
            return null;
        } else {
            return GenericJsonConverter.jsonToObject(this.snapItems, new TypeReference<List<OrderSku>>() {});
        }
    }

    public void setSnapItems(List<OrderSku> specs) {
        if (specs.isEmpty()) {
            this.snapItems = "";
        } else {
            this.snapItems = GenericJsonConverter.objectToJson(specs);
        }
    }

    public OrderAddressDTO getSnapAddress() {
        if (this.snapAddress == null) {
            return null;
        } else {
            return GenericJsonConverter.jsonToObject(this.snapAddress, new TypeReference<OrderAddressDTO>() {});
        }
    }

    public void setSnapAddress(OrderAddressDTO orderAddressDTO) {
        if (orderAddressDTO == null) {
            this.snapAddress = "";
        } else {
            this.snapAddress = GenericJsonConverter.objectToJson(orderAddressDTO);
        }
    }
}
