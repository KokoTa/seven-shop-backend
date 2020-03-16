package com.example.shop.service;

import com.example.shop.model.Spu;
import com.example.shop.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpuService {

    @Autowired
    SpuRepository spuRepository;

    public Spu getSpuById(Long id) {
        return spuRepository.findOneById(id);
    }

    public List<Spu> getLatestSpuListByPage() {
        return spuRepository.findAll();
    }
}
