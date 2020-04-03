package com.example.shop.api.v1;

import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.core.enumeration.CouponStatus;
import com.example.shop.core.local.LocalUser;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.Coupon;
import com.example.shop.model.UserCoupon;
import com.example.shop.service.CouponService;
import com.example.shop.vo.CouponCategoryVO;
import com.example.shop.vo.CouponPureVO;
import com.example.shop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/coupon")
public class CouponController {

    @Autowired
    private CouponService couponService;

    /**
     * 通过分类获取优惠券
     * @param cid 只能传入二级分类的 id
     * @return
     */
    @GetMapping("/by/category/{cid}")
    public List<CouponPureVO> getCouponListByCategory(
            @PathVariable Long cid
    ) {
        List<Coupon> coupons = couponService.getByCategory(cid);
        if (coupons.isEmpty()) return Collections.emptyList();
        return CouponPureVO.getList(coupons);
    }

    /**
     * 获取全场优惠券
     * @return
     */
    @GetMapping("/whole_store")
    public List<CouponPureVO> getWholeStoreCouponList() {
        List<Coupon> coupons = couponService.getWholeStoreCoupons();
        if (coupons.isEmpty()) return Collections.emptyList();
        return CouponPureVO.getList(coupons);
    }

    /**
     * 领取优惠券，需要登录、用户信息、优惠券信息
     * @param id 优惠券 id
     */
    @ScopeLevel()// 权限默认是 5
    @PostMapping("/collect/{id}")
    public ResultVO collectCoupon(
            @PathVariable Long id
    ) {
        Long uid = LocalUser.getUser().getId();
        UserCoupon userCoupon = couponService.collectOneCoupon(uid, id);
        return ResultVO.builder()
                .code(200)
                .data(userCoupon)
                .message("领取成功")
                .build();
    }


    /**
     * 通过状态获取优惠券
     * @param status
     * @return
     */
    @ScopeLevel
    @GetMapping("/myself/by/status/{status}")
    public List<CouponPureVO> getMyCouponByStatus(
            @PathVariable Integer status
    ) {
        Long uid = LocalUser.getUser().getId();
        List<Coupon>  couponList;

        switch (CouponStatus.toType(status)) {
            case AVAILABLE:
                couponList = couponService.getMyAvailableCoupons(uid);
                break;
            case USED:
                couponList = couponService.getMyUsedCoupons(uid);
                break;
            case EXPIRED:
                couponList = couponService.getMyExpiredCoupons(uid);
                break;
            default:
                throw new ParameterException(40001);
        }

        return CouponPureVO.getList(couponList);
    }

    /**
     * 获取用户可用优惠券（带分类）
     * @return
     */
    @ScopeLevel
    @GetMapping("/myself/available/with_category")
    public List<CouponCategoryVO> getUserCouponWithCategory() {
        Long uid = LocalUser.getUser().getId();
        List<Coupon> coupons = couponService.getMyAvailableCoupons(uid);
        if (coupons.isEmpty()) {
            return Collections.emptyList();
        }
        return coupons.stream().map(CouponCategoryVO::new).collect(Collectors.toList());
    }

}
