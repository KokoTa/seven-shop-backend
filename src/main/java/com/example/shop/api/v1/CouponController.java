package com.example.shop.api.v1;

import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.core.local.LocalUser;
import com.example.shop.model.Coupon;
import com.example.shop.model.UserCoupon;
import com.example.shop.service.CouponService;
import com.example.shop.vo.CouponPureVO;
import com.example.shop.vo.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

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
        UserCoupon userCoupon = couponService.collectOneCoupon(id, uid);
        return ResultVO.builder()
                .code(200)
                .data(userCoupon)
                .message("领取成功")
                .build();
    }

}
