package com.example.shop.service;

import com.example.shop.model.Activity;
import com.example.shop.repository.ActivityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    public Activity getByName(String name) {
        return activityRepository.findByName(name);
    }
}
