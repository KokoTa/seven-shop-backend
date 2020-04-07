package com.example.shop.service;

import com.example.shop.core.enumeration.OrderStatus;
import com.example.shop.core.money.IMoney;
import com.example.shop.dto.OrderDTO;
import com.example.shop.dto.SkuInfoDTO;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.logic.CouponChecker;
import com.example.shop.logic.OrderChecker;
import com.example.shop.model.*;
import com.example.shop.repository.CouponRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.SkuRepository;
import com.example.shop.repository.UserCouponRepository;
import com.example.shop.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private IMoney upRound;

    @Value("${condition.max-sku-limit}")
    private Integer maxSkuLimit;

    /**
     * 订单数据是否正确
     */
    public OrderChecker isOk(Long uid, OrderDTO orderDTO) {
        // 总价如果 <=0 则报错
        if (orderDTO.getFinalTotalPrice().compareTo(new BigDecimal("0")) <= 0) {
            throw new ParameterException(50001);
        }

        List<Long> skuIdList = orderDTO.getSkuInfoList()
                .stream()
                .map(SkuInfoDTO::getId)
                .collect(Collectors.toList());
        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);

        Long couponId = orderDTO.getCouponId();
        CouponChecker couponChecker = null;

        if (couponId != null) {
            // 报错表示没有这张优惠券
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(40003));
            // 报错表示用户没有这张优惠券
            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndCouponIdAndOrderIdIsNull(uid, couponId).orElseThrow(() -> new NotFoundException(40007));
            // 优惠券校验器
            couponChecker = new CouponChecker(coupon, upRound);
        }

        // 订单校验器
        OrderChecker orderChecker = new OrderChecker(orderDTO, skuList, couponChecker, maxSkuLimit);
        orderChecker.isOK();
        return orderChecker;
    }

    /**
     * 创建订单
     */
    @Transactional
    public Long placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker) {
        String orderNo = OrderUtil.makeOrderNo(); // 生成订单号

        // 构建订单
        Order order = Order.builder()
                .orderNo(orderNo)
                .totalPrice(orderDTO.getTotalPrice())
                .finalTotalPrice(orderDTO.getFinalTotalPrice())
                .userId(uid)
                .totalCount(orderChecker.getTotalCount())
                .snapImg(orderChecker.getSnapImg())
                .snapTitle(orderChecker.getSnapTitle())
                .status(OrderStatus.UNPAID.value())
                .build();

        order.setSnapItems(orderChecker.getOrderSkuList());
        order.setSnapAddress(orderDTO.getAddress());

        // 创建订单
        orderRepository.save(order);
        // 减去库存
        reduceStock(orderChecker);
        // 核销优惠券
        if (orderDTO.getCouponId() != null) {
            changeCouponStatus(orderDTO.getCouponId(), order.getId(), uid);
        }
        // 信息加入到延迟消息队列（未付款，时间到后要归还库存和优惠券）

        return order.getId();
    }

    private void reduceStock(OrderChecker orderChecker) {
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for (OrderSku orderSku : orderSkuList) {
            Integer result = skuRepository.reduceStock(orderSku.getId(), orderSku.getCount());
            if (result != 1) throw new ParameterException(50003);
        }
    }

    private void changeCouponStatus(Long couponId, Long orderId, Long uid) {
        Integer result = userCouponRepository.changeCouponStatus(couponId, orderId, uid);
        if (result != 1) throw new ParameterException(50006);
    }
}
