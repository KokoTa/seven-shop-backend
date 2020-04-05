package com.example.shop.api.v1;

import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.core.local.LocalUser;
import com.example.shop.dto.OrderDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/order")
@Validated
public class OrderController {

    @ScopeLevel
    @PostMapping("/")
    public void placeOrder(
            @RequestBody @Valid OrderDTO orderDTO
    ) {
        Long uid = LocalUser.getUser().getId();
    }
}
