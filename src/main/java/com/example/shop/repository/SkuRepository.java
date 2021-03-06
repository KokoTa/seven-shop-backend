package com.example.shop.repository;

import com.example.shop.model.Sku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, Long> {

    List<Sku> findAllByIdIn(List<Long> ids);

    @Modifying
    @Query("update Sku s \n" +
            "set s.stock = s.stock - :quantity\n" +
            "where s.id = :sid\n" +
            "and s.stock >= :quantity")
    Integer reduceStock(Long sid, Long quantity);

    @Modifying
    @Query("update Sku s \n" +
            "set s.stock = s.stock + :quantity\n" +
            "where s.id = :sid")
    Integer recoverStock(Long sid, Long quantity);
}
