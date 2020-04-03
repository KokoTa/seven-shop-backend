package com.example.shop.service;

import com.example.shop.model.Sku;
import com.example.shop.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkuService {

    @Autowired
    SkuRepository skuRepository;

    public List<Sku> getSkuListByIds(List<Long> ids) {
        return skuRepository.findAllByIdIn(ids);
    }
}
