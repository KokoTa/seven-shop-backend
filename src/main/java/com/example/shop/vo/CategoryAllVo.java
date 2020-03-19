package com.example.shop.vo;

import com.example.shop.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoryAllVo {
    private List<CategoryPureVo> roots;
    private List<CategoryPureVo> subs;

    public CategoryAllVo(Map<String, List<Category>> map) {
        List<Category> roots = map.get("roots");
        List<Category> subs = map.get("subs");

        this.roots = roots.stream().map(CategoryPureVo::new).collect(Collectors.toList());
        this.subs = subs.stream().map(CategoryPureVo::new).collect(Collectors.toList());
    }
}
