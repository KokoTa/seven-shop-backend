package com.example.shop.service;

import com.example.shop.model.Spu;
import com.example.shop.repository.SpuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SpuService {

    @Autowired
    SpuRepository spuRepository;

    public Spu getSpuById(Long id) {
        return spuRepository.findOneById(id);
    }

    public Page<Spu> getLatestSpuListByPage(Integer pageNo, Integer pageSize) {
        Pageable page = PageRequest.of(pageNo, pageSize, Sort.by("createTime").descending());
        return spuRepository.findAll(page);
    }

    public Page<Spu> getSpuListByCategoryId(Long id, Boolean isRoot, Integer pageNo, Integer pageSize) {
        Pageable page = PageRequest.of(pageNo, pageSize);
        Page<Spu> spuPage = null;

        if (isRoot) {
            spuPage = spuRepository.findAllByRootCategoryIdOrderByCreateTime(id, page);
        } else {
            spuPage = spuRepository.findAllByCategoryIdOrderByCreateTime(id, page);
        }

        return spuPage;
    }
}
