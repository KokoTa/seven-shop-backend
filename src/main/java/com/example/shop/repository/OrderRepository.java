package com.example.shop.repository;

import com.example.shop.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findAllByExpiredTimeGreaterThanAndStatusAndUserId(Date now, Integer Status, Long uid,
            Pageable pageable);

    Page<Order> findByUserId(Long uid, Pageable pageable);

    Page<Order> findByUserIdAndStatus(Long uid, Integer status, Pageable pageable);

    Optional<Order> findFirstByUserIdAndId(Long uid, Long oid);

    Optional<Order> findFirstByOrderNo(String orderNo);

    @Modifying
    @Query("update Order o set o.status=:status\n" +
            "where o.orderNo=:orderNo")
    Integer updateStatusByOrderNo(String orderNo, Integer status);

    @Modifying
    @Query("update Order o set o.status=5\n" +
            "where o.status = 1\n" +
            "and o.id = :oid")
    Integer cancelOrder(Long oid);
}
