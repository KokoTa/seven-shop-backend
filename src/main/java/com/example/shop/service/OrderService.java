package com.example.shop.service;

import com.example.shop.dto.OrderDTO;
import com.example.shop.dto.SkuInfoDTO;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.logic.CouponChecker;
import com.example.shop.model.Coupon;
import com.example.shop.model.Sku;
import com.example.shop.model.UserCoupon;
import com.example.shop.repository.CouponRepository;
import com.example.shop.repository.UserCouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    /**
     * 订单数据是否正确
     */
    public void isOk(Long uid, OrderDTO orderDTO) {
        // 总价如果 <=0 则报错
        if (orderDTO.getFinalTotalPrice().compareTo(new BigDecimal("0")) <= 0) {
            throw new ParameterException(50001);
        }

        // 数据库找到购买的商品，计算总价，和前端传过来的总价进行对比，不一致就报错
        List<Long> skuIdList = orderDTO.getSkuInfoList()
                .stream()
                .map(SkuInfoDTO::getId)
                .collect(Collectors.toList());

        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);

        Long couponId = orderDTO.getCouponId();
        CouponChecker couponChecker = null;

        // 如果订单使用了优惠券
        if (couponId != null) {
            // 报错表示没有这张优惠券
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(40003));
            // 报错表示用户没有这张优惠券
            UserCoupon userCoupon = userCouponRepository.findFirstByUserIdAndCouponId(uid, couponId).orElseThrow(() -> new NotFoundException(40007));
            couponChecker = new CouponChecker(coupon, userCoupon);
        }
    }
}
