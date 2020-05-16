package com.example.shop.service;

import com.example.shop.bo.OrderMessageBO;
import com.example.shop.core.enumeration.OrderStatus;
import com.example.shop.exception.http.ServerException;
import com.example.shop.model.Order;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CouponBackService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserCouponRepository userCouponRepository;

    @Transactional
    public void returnBack(OrderMessageBO orderMessageBO) {
        Long userId = orderMessageBO.getUserId();
        Long orderId = orderMessageBO.getOrderId();
        Long couponId = orderMessageBO.getCouponId();

        // 没有使用优惠券就不用归还优惠券
        if (couponId == -1) return;

        Optional<Order> optionalOrder = orderRepository.findFirstByUserIdAndId(userId, orderId);
        Order order = optionalOrder.orElseThrow(() -> new ServerException(50012));

        // 未支付或已取消的订单要归还优惠券
        // 这里加入已取消是因为可能归还订单的逻辑会先执行，先让订单状态变为已取消了
        if (order.getStatus().equals(OrderStatus.UNPAID.value()) ||
            order.getStatus().equals(OrderStatus.CANCELED.value())) {
            userCouponRepository.returnBack(couponId, userId);
        }
    }
}
