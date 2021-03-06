package com.example.shop.repository;

import com.example.shop.model.Coupon;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    // c 是 Coupon 实体的别名
    // join 导航属性(categoryList) 就相当于 join 了 category 表
    @Query(value = "select c from Coupon c\n" +
            "join\n" +
            "c.categoryList ca\n" +
            "join\n" +
            "Activity a on a.id = c.activityId\n" +
            "where ca.id = :cid\n" +
            "and a.startTime < :now\n" +
            "and a.endTime > :now")
    List<Coupon> findByCategory(Long cid, Date now, Pageable pageable);

    @Query(value = "select c from Coupon c\n" +
            "join\n" +
            "Activity a on c.activityId = a.id\n" +
            "where c.wholeStore = :isWholeStore\n" +
            "and a.startTime < :now\n" +
            "and a.endTime > :now\n" +
            "\n")
    List<Coupon> findByWholeStore(Boolean isWholeStore, Date now);

    @Query("select c from Coupon c\n" +
            "join\n" +
            "UserCoupon uc on uc.couponId = c.id\n" +
            "join\n" +
            "User u on uc.userId = u.id\n" +
            "where u.id = :uid\n" +
            "and uc.status = 1\n" +
            "and c.startTime < :now\n" +
            "and c.endTime > :now\n" +
            "and uc.orderId is null")
    List<Coupon> findMyAvailable(Long uid, Date now);

    @Query("select c from Coupon c\n" +
            "join\n" +
            "UserCoupon uc on uc.couponId = c.id\n" +
            "where uc.userId = :uid\n" +
            "and uc.status = 2\n" +
            "and c.startTime < :now\n" +
            "and c.endTime > :now\n" +
            "and uc.orderId is not null")
    List<Coupon> findMyUsed(Long uid, Date now);

    @Query("select c from Coupon c\n" +
            "join\n" +
            "UserCoupon uc on uc.couponId = c.id\n" +
            "where uc.userId = :uid\n" +
            "and uc.status <> 2\n" +
            "and c.endTime < :now\n" +
            "and uc.orderId is null")
    List<Coupon> findMyExpired(Long uid, Date now);
}
