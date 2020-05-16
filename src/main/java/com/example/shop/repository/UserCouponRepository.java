package com.example.shop.repository;

import com.example.shop.model.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    Optional<UserCoupon> findFirstByUserIdAndCouponId(Long uid, Long couponId);

    Optional<UserCoupon> findFirstByUserIdAndCouponIdAndOrderIdIsNull(Long uid, Long couponId);

    @Modifying
    @Query("update UserCoupon uc\n" +
            "set uc.status = 2, uc.orderId = :oid\n" +
            "where uc.userId = :uid\n" +
            "and uc.couponId = :cid\n" +
            "and uc.status = 1\n" +
            "and uc.orderId is null")
    Integer changeCouponStatus(Long cid, Long oid, Long uid);

    @Modifying
    @Query("update UserCoupon uc\n" +
            "set uc.status = 1, uc.orderId = null\n" +
            "where uc.couponId = :cid\n" +
            "and uc.userId = :uid\n" +
            "and uc.status = 2\n" +
            "and uc.orderId is not null")
    Integer returnBack(Long cid, Long uid);
}
