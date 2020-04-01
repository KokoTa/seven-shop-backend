package com.example.shop.vo;

import com.example.shop.model.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
public class CouponPureVO {
    private Long id;
    private String title;
    private Date startTime;
    private Date endTime;
    private String description;
    private BigDecimal fullMoney;
    private BigDecimal minus;
    private BigDecimal rate;
    private Integer type;
    private String remark;
    private Boolean wholeStore;

    public CouponPureVO(Coupon coupon) {
        BeanUtils.copyProperties(coupon, this);
    }
}
