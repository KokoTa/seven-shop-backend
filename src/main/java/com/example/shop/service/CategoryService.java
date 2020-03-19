package com.example.shop.service;

import com.example.shop.model.Category;
import com.example.shop.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    public Map<String, List<Category>> getAllCategory() {
        List<Category> rootCategory = categoryRepository.findAllByIsRootOrderByIndexAsc(true);
        List<Category> subCategory = categoryRepository.findAllByIsRootOrderByIndexAsc(false);
        Map<String, List<Category>> map = new HashMap<>();

        map.put("roots", rootCategory);
        map.put("subs", subCategory);

        return map;
    }
}
