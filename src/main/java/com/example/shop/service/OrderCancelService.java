package com.example.shop.service;

import com.example.shop.bo.OrderMessageBO;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.Order;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderCancelService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    SkuRepository skuRepository;

    /**
     * 1. 归还库存
     * 2. 改变订单状态
     * @param orderMessageBO 消息队列返回过来被解析后的数据
     */
    @Transactional
    public void cancel(OrderMessageBO orderMessageBO) {
        Long orderId = orderMessageBO.getOrderId();

        Optional<Order> orderOptional = orderRepository.findById(orderId);
        // 这里报的异常是 HTTP 异常，是给前端的，但该方法是消息队列触发的
        // 可以新建一个异常类来处理服务端的内部异常，项目里偷懒了没写
        Order order = orderOptional.orElseThrow(() -> new ParameterException(50012));
        // 取消订单
        int res = orderRepository.cancelOrder(orderId);
        // 归还库存
        order.getSnapItems().forEach((item) -> {
            skuRepository.recoverStock(item.getId(), item.getCount());
        });
    }
}
