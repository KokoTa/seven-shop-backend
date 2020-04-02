package com.example.shop.repository;

import com.example.shop.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Activity findByName(String name);

    // 这里 ByCouponList 相当于连表查询
    Optional<Activity> findByCouponListId(Long couponId);
}
