package com.example.shop.service;

import com.example.shop.model.GridCategory;
import com.example.shop.repository.GridCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GridCategoryService {

    @Autowired
    GridCategoryRepository gridCategoryRepository;

    public List<GridCategory> getAllGridCategory() {
        return gridCategoryRepository.findAll();
    }
}
