package com.example.shop.service;

import com.example.shop.model.Coupon;
import com.example.shop.repository.ActivityRepository;
import com.example.shop.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ActivityRepository activityRepository;

    public List<Coupon> getByCategory(Long cid) {
        Date now = new Date(); // 查找的优惠券不能是过期的
        return couponRepository.findByCategory(cid, now);
    }
}
