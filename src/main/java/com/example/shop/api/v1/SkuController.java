package com.example.shop.api.v1;

import com.example.shop.model.Sku;
import com.example.shop.service.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/sku")
public class SkuController {

    @Autowired
    SkuService skuService;

    @GetMapping("/skus")
    public List<Sku> getSkus(@RequestParam(name = "ids") String ids) {
        String[] strings = ids.split(",");
        List<Long> array = new ArrayList<>();
        Arrays.stream(strings).forEach(s -> {
            array.add(Long.parseLong(s));
        });
        return skuService.getSkuListByIds(array);
    }
}
