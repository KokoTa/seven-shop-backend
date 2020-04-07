package com.example.shop.service;

import com.example.shop.core.enumeration.CouponStatus;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.Activity;
import com.example.shop.model.Coupon;
import com.example.shop.model.UserCoupon;
import com.example.shop.repository.ActivityRepository;
import com.example.shop.repository.CouponRepository;
import com.example.shop.repository.UserCouponRepository;
import com.example.shop.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    public List<Coupon> getByCategory(Long cid) {
        Date now = new Date(); // 查找的优惠券不能是过期的
        return couponRepository.findByCategory(cid, now, PageRequest.of(0, 10));
    }

    public List<Coupon> getWholeStoreCoupons() {
        Date now = new Date(); // 查找的优惠券不能是过期的
        return couponRepository.findByWholeStore(true, now);
    }

    public UserCoupon collectOneCoupon(Long uid, Long couponId) {
        // 检查是否有对应的优惠券，没有就会报错
        couponRepository.findById(couponId).orElseThrow(() -> new ParameterException(40003));

        // 检查领取日期是否过期
        Activity activity = activityRepository.findByCouponListId(couponId).orElseThrow(() -> new ParameterException(40004));
        Date now = new Date();
        Boolean isValid = CommonUtil.isInTime(now, activity.getStartTime(), activity.getEndTime());
        if (!isValid) throw new NotFoundException(40005);

        // 检查用户是否持有该优惠券
        userCouponRepository.findFirstByUserIdAndCouponIdAndOrderIdIsNull(uid, couponId).ifPresent((uc) -> {
            throw new ParameterException(40006);
        });

        // 给用户加优惠券
        UserCoupon userCoupon = UserCoupon.builder()
                .userId(uid)
                .couponId(couponId)
                .createTime(now)
                .status(CouponStatus.AVAILABLE.getValue())
                .build();

        userCouponRepository.save(userCoupon);

        return userCoupon;
    }

    public List<Coupon> getMyAvailableCoupons(Long id) {
        Date now = new Date();
        return couponRepository.findMyAvailable(id, now);
    }

    public List<Coupon> getMyUsedCoupons(Long id) {
        Date now = new Date();
        return couponRepository.findMyUsed(id, now);
    }

    public List<Coupon> getMyExpiredCoupons(Long id) {
        Date now = new Date();
        return couponRepository.findMyExpired(id, now);
    }
}
