package com.example.shop.repository;

import com.example.shop.model.Spu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpuRepository extends JpaRepository<Spu, Long> {

    Spu findOneById(Long id);

    // 查找一级分类 SPU
    Page<Spu> findAllByRootCategoryIdOrderByCreateTime(Long id, Pageable pageable);

    // 查找二级分类的 SPU，只有二级分类才有 category id
    Page<Spu> findAllByCategoryIdOrderByCreateTime(Long id, Pageable pageable);

}
