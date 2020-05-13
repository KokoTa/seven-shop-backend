package com.example.shop.model;

import com.example.shop.core.enumeration.OrderStatus;
import com.example.shop.dto.OrderAddressDTO;
import com.example.shop.util.CommonUtil;
import com.example.shop.util.GenericJsonConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "delete_time is null")
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
    private Date expiredTime; // 订单过期时间
    private Date placedTime; // 订单创建时间

    private String snapItems;
    private String snapAddress;

    public List<OrderSku> getSnapItems() {
        if (this.snapItems == null) {
            return null;
        } else {
            return GenericJsonConverter.jsonToObject(this.snapItems, new TypeReference<List<OrderSku>>() {
            });
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
            return GenericJsonConverter.jsonToObject(this.snapAddress, new TypeReference<OrderAddressDTO>() {
            });
        }
    }
    public void setSnapAddress(OrderAddressDTO orderAddressDTO) {
        if (orderAddressDTO == null) {
            this.snapAddress = "";
        } else {
            this.snapAddress = GenericJsonConverter.objectToJson(orderAddressDTO);
        }
    }

    /**
     * 判断订单是否合法
     * @return
     */
    public Boolean isValid() {
        // 非待支付的订单不合法
        if (!this.getStatus().equals((OrderStatus.UNPAID.value()))) return true;
        // 过期的订单不合法，未过期的订单合法
        // isOutOfDate 有两个方法，一个是传入 订单创建时间 + 配置文件的过期时间间隔，另一个是传入 订单过期时间
        return CommonUtil.isOutOfDate(this.getExpiredTime());
    }

}
