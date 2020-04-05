package com.example.shop.logic;

import com.example.shop.core.enumeration.CouponType;
import com.example.shop.core.money.IMoney;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.Coupon;
import com.example.shop.model.UserCoupon;
import com.example.shop.util.CommonUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

public class CouponChecker {

    private Coupon coupon;
    private UserCoupon userCoupon;
    private IMoney iMoney;

    public CouponChecker(Coupon coupon, UserCoupon userCoupon, IMoney iMoney) {
        this.coupon = coupon;
        this.userCoupon = userCoupon;
        this.iMoney = iMoney;
    }

    // 验证优惠券是否过期
    public void timeRangeOk() {
        Date now = new Date();
        Boolean isInTime = CommonUtil.isInTime(now, coupon.getStartTime(), coupon.getEndTime());
        if (!isInTime) throw new ParameterException(40005);
    }

    // 验证前端传过来的总价是否和服务端计算出来的总价一致
    public void finalTotalPriceOk(BigDecimal frontFinalTotalPrice, BigDecimal serverTotalPrice) {
        BigDecimal serverFinalTotalPrice = null;

        switch (CouponType.toType(coupon.getType())) {
            case FULL_MINUS:
            case NO_THRESHOLD_MINUS:
                serverFinalTotalPrice = serverTotalPrice.subtract(coupon.getMinus());
                if (serverFinalTotalPrice.compareTo(new BigDecimal("0")) < 0) throw new ParameterException(50008);
                break;
            case FULL_OFF:
                serverFinalTotalPrice = iMoney.dicount(serverTotalPrice, coupon.getRate());
                break;
            default:
                throw new ParameterException(50009);
        }

        int compare = serverFinalTotalPrice.compareTo(frontFinalTotalPrice);
        if (compare != 0) throw new ParameterException(50008);
    }

    public void canBeUsed() {}
}
