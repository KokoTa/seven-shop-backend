package com.example.shop.api.v1;

import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.core.local.LocalUser;
import com.example.shop.dto.OrderDTO;
import com.example.shop.logic.OrderChecker;
import com.example.shop.service.OrderService;
import com.example.shop.vo.OrderIdVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ScopeLevel
    @PostMapping("")
    public OrderIdVO placeOrder(@RequestBody @Validated OrderDTO orderDTO) {
        Long uid = LocalUser.getUser().getId();
        OrderChecker orderChecker = orderService.isOk(uid, orderDTO);
        Long orderId = orderService.placeOrder(uid, orderDTO, orderChecker);
        return new OrderIdVO(orderId);
    }
}
