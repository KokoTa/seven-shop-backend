package com.example.shop.vo;

import com.example.shop.model.Activity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class ActivityCouponVO extends ActivityPureVO {
    private List<CouponPureVO> coupons;

    public ActivityCouponVO(Activity activity) {
        super(activity);
        coupons = activity.getCouponList()
                .stream().map(CouponPureVO::new)
                .collect(Collectors.toList());
    }
}
