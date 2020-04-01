package com.example.shop.repository;

import com.example.shop.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String name);
}
