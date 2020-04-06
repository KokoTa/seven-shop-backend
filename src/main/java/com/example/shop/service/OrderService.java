package com.example.shop.service;

import com.example.shop.core.money.IMoney;
import com.example.shop.dto.OrderDTO;
import com.example.shop.dto.SkuInfoDTO;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.logic.CouponChecker;
import com.example.shop.logic.OrderChecker;
import com.example.shop.model.Coupon;
import com.example.shop.model.Sku;
import com.example.shop.model.UserCoupon;
import com.example.shop.repository.CouponRepository;
import com.example.shop.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndCouponId(uid, couponId).orElseThrow(() -> new NotFoundException(40007));
            // 优惠券校验器
            couponChecker = new CouponChecker(coupon, upRound);
        }

        // 订单校验器
        OrderChecker orderChecker = new OrderChecker(orderDTO, skuList, couponChecker, maxSkuLimit);
        orderChecker.isOK();
        return orderChecker;
    }
}
