package com.example.shop.logic;

import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.Coupon;
import com.example.shop.model.UserCoupon;
import com.example.shop.util.CommonUtil;

import java.util.Date;

public class CouponChecker {

    private Coupon coupon;
    private UserCoupon userCoupon;

    public CouponChecker(Coupon coupon, UserCoupon userCoupon) {
        this.coupon = coupon;
        this.userCoupon = userCoupon;
    }

    public void timeRangeOk() {
        Date now = new Date();
        Boolean isInTime = CommonUtil.isInTime(now, coupon.getStartTime(), coupon.getEndTime());
        if (!isInTime) throw new ParameterException(40005);
    }

    public void finalTotalPriceOk() {}

    public void canBeUsed() {}
}
