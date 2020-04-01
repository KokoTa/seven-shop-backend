package com.example.shop.api.v1;

import com.example.shop.model.Coupon;
import com.example.shop.service.CouponService;
import com.example.shop.vo.CouponPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
