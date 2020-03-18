package com.example.shop.api.v1;

import com.example.shop.bo.PageCounter;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.model.Spu;
import com.example.shop.service.SpuService;
import com.example.shop.util.CommonUtil;
import com.example.shop.vo.PagingDozer;
import com.example.shop.vo.SpuSimpleVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/spu")
@Validated
public class SpuController {

    @Autowired
    SpuService spuService;

    /**
     * 根据 ID 获取 SPU
     * @param id
     * @return Spu
     */
    @GetMapping("/id/{id}/detail")
    public Spu getSpuById(@PathVariable @Positive Long id) {
        Spu spu = spuService.getSpuById(id);
        if (spu == null) {
            throw new NotFoundException(30003);
        }
        return spu;
    }

    /**
     * 根据 ID 获取 SPU，用 Vo 返回
     * @param id
     * @return SpuSimpleVo
     */
    @GetMapping("/getSimpleSpu/{id}")
    public SpuSimpleVo getSimpleSpu(@PathVariable @Positive Long id) {
        Spu spu = spuService.getSpuById(id);
        SpuSimpleVo vo = new SpuSimpleVo();
        BeanUtils.copyProperties(spu, vo); // 只能浅拷贝
        return vo;
    }

    /**
     * 获取 SPU 列表
     * @return List<Spu>
     */
    @GetMapping("/latest")
    public PagingDozer<Spu, SpuSimpleVo> getLatestSpuList(
            @RequestParam(name = "start", defaultValue = "0") Integer start,
            @RequestParam(name = "count", defaultValue = "10") Integer count
    ) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Spu> page = spuService.getLatestSpuListByPage(pageCounter.getPageNo(), pageCounter.getPageSize());
        PagingDozer<Spu, SpuSimpleVo> pagingDozer = new PagingDozer<>(page, SpuSimpleVo.class);

        return pagingDozer;
    }

    @GetMapping("/category/{id}")
    public PagingDozer<Spu, SpuSimpleVo> getSpuListByCategoryId(
            @PathVariable(name = "id") @Positive Long id,
            @RequestParam(name = "isRoot") Boolean isRoot,
            @RequestParam(name = "start", defaultValue = "0") Integer start,
            @RequestParam(name = "count", defaultValue = "10") Integer count
    ) {
        PageCounter pageCounter = CommonUtil.convertToPageParameter(start, count);
        Page<Spu> page = spuService.getSpuListByCategoryId(id, isRoot, pageCounter.getPageNo(), pageCounter.getPageSize());
        PagingDozer<Spu, SpuSimpleVo> pagingDozer = new PagingDozer<>(page, SpuSimpleVo.class);

        return pagingDozer;
    }
}
