package com.example.shop.service;

import com.example.shop.model.Sku;
import com.example.shop.repository.SkuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SkuService {

    @Autowired
    SkuRepository skuRepository;

    public List<Sku> getSkuListByIds(List<Long> ids) {
        // 升序
        ids.sort(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1.compareTo(o2);
            }
        });
        return skuRepository.findAllByIdIn(ids);
    }
}
