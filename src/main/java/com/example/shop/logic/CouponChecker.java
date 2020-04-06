package com.example.shop.logic;

import com.example.shop.bo.SkuOrderBO;
import com.example.shop.core.enumeration.CouponType;
import com.example.shop.core.money.IMoney;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.Category;
import com.example.shop.model.Coupon;
import com.example.shop.util.CommonUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券校验
 * 1. 验证优惠券是否过期
 * 2. 验证前端使用优惠券后的价格和后端使用优惠券后的价格是否一致
 * 2. 验证优惠券是否满足使用门槛
 */
public class CouponChecker {

    private Coupon coupon;
    private IMoney iMoney;

    public CouponChecker(Coupon coupon, IMoney iMoney) {
        this.coupon = coupon;
        this.iMoney = iMoney;
    }

    // 验证优惠券是否过期
    public void timeRangeOK() {
        Date now = new Date();
        Boolean isInTime = CommonUtil.isInTime(now, coupon.getStartTime(), coupon.getEndTime());
        if (!isInTime) throw new ParameterException(40005);
    }

    /**
     * 验证前端使用优惠券后的价格和后端使用优惠券后的价格是否一致
     * @param frontFinalTotalPrice 前端传过来的最终总价格(使用优惠券后的价格)
     * @param serverTotalPrice 服务端的原始总价格(未使用优惠券的价格)
     */
    public void finalTotalPriceOK(BigDecimal frontFinalTotalPrice, BigDecimal serverTotalPrice) {
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

    /**
     * 验证优惠券是否满足使用门槛
     * @param skuOrderBOList sku信息(价格、数量、分类)
     */
    public void canBeUsed(List<SkuOrderBO> skuOrderBOList) {
        // 全场券没有使用门槛
        if (!coupon.getWholeStore()) {
            // 非全场券，需要找出符合优惠券类型的sku，然后累加他们的价格，接着判断是否满足门槛金额
            List<Long> cidList = coupon.getCategoryList().stream()
                    .map(Category::getId)
                    .collect(Collectors.toList());
            BigDecimal categoryTotalPrice = getSumByCategoryList(skuOrderBOList, cidList);

            // 如果不满足门槛金额就会报错
            switch (CouponType.toType(coupon.getType())) {
                case FULL_OFF:
                case FULL_MINUS:
                    int compare = categoryTotalPrice.compareTo(coupon.getFullMoney()); // 是否到达满减门槛
                    if (compare < 0) throw new ParameterException(50010); // 未到达门槛就报错
                    break;
                case NO_THRESHOLD_MINUS: // 全场券直接略过
                    break;
                default:
                    throw new ParameterException(50009); // 优惠券类型错误报错
            }
        }
    }

    /**
     * 累加不同分类下的 sku 价格
     * @param skuOrderBOList skuOrderBO 集合
     * @param cidList 分类 id 集合
     * @return
     */
    private BigDecimal getSumByCategoryList(List<SkuOrderBO> skuOrderBOList, List<Long> cidList) {
        return cidList.stream()
                .map(cid -> getSumByCategory(skuOrderBOList, cid))
                .reduce(BigDecimal::add)
                .orElse(new BigDecimal("0"));
    }

    /**
     * 累加同分类下的 sku 价格
     * @param skuOrderBOList skuOrderBO 集合
     * @param cid 分类 id
     * @return
     */
    private BigDecimal getSumByCategory(List<SkuOrderBO> skuOrderBOList, Long cid) {
        return skuOrderBOList.stream()
                .filter(sku -> sku.getCategoryId().equals(cid)) // 找出通类的 sku
                .map(SkuOrderBO::getTotalPrice) // 抽取总价
                .reduce(BigDecimal::add) // 累加总价
                .orElse(new BigDecimal("0"));
    }
}
