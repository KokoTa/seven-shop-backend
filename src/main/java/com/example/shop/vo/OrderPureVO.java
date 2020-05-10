package com.example.shop.vo;

import com.example.shop.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
@NoArgsConstructor
public class OrderPureVO extends Order {
    private Long period;

    public OrderPureVO(Order order, Long payTimeLimit) {
        BeanUtils.copyProperties(order, this);
        this.period = payTimeLimit;
    }
}
