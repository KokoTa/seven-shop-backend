package com.example.shop.api.v1;

import com.example.shop.exception.http.NotFoundException;
import com.example.shop.model.Spu;
import com.example.shop.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {

    @Autowired
    SpuService spuService;

    @GetMapping("/id/{id}/detail")
    public Spu getSpuById(@PathVariable @Positive Long id) {
        Spu spu = spuService.getSpuById(id);
        if (spu == null) {
            throw new NotFoundException(30003);
        }
        return spu;
    }

    @GetMapping("/latest")
    public List<Spu> getLatestSpuList() {
        List<Spu> list = spuService.getLatestSpuListByPage();
        if (list == null) {
            throw new NotFoundException(10000);
        }
        return list;
    }
}
