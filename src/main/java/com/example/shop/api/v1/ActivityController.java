package com.example.shop.api.v1;

import com.example.shop.exception.http.NotFoundException;
import com.example.shop.model.Activity;
import com.example.shop.service.ActivityService;
import com.example.shop.vo.ActivityCouponVO;
import com.example.shop.vo.ActivityPureVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/name/{name}")
    public ActivityPureVO getHomeActivity(
            @PathVariable(name = "name") String name
    ) {
        Activity activity = activityService.getByName(name);
        if (activity == null) throw new NotFoundException(10000);
        return new ActivityPureVO(activity);
    }

    @GetMapping("/name/{name}/with_coupon")
    public ActivityCouponVO getActivityWithCoupons(
            @PathVariable(name = "name") String name
    ) {
        Activity activity = activityService.getByName(name);
        if (activity == null) throw new NotFoundException(10000);
        return new ActivityCouponVO(activity);
    }

}
