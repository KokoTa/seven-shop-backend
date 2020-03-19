package com.example.shop.repository;

import com.example.shop.model.Theme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ThemeRepository extends JpaRepository<Theme, Long> {

    // JPQL(Java 实体查询语言，和原生 SQL 有所不同，Theme 指代的是实体而不是表)
    @Query("select t from Theme t where t.name in (:nameList)")
    List<Theme> findByNames(@Param("nameList") List<String> names);

    Optional<Theme> findByName(String name);
}
