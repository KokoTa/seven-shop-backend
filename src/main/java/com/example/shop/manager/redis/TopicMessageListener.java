package com.example.shop.manager.redis;

import com.example.shop.bo.OrderMessageBO;
import com.example.shop.service.CouponBackService;
import com.example.shop.service.OrderCancelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class TopicMessageListener implements MessageListener {

    @Autowired
    private OrderCancelService orderCancelService;

    @Autowired
    private CouponBackService couponBackService;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        byte[] body = message.getBody(); // 获取键
        byte[] channel = message.getChannel(); // 获取监听规则
        String expiredKey = new String(body);
        String topic = new String(channel);

        System.out.println(expiredKey);
        System.out.println(topic);

        OrderMessageBO orderMessageBO = new OrderMessageBO(expiredKey);
        // 取消订单，归还库存
        orderCancelService.cancel(orderMessageBO);
        // 归还优惠券
        couponBackService.returnBack(orderMessageBO);
    }
}
