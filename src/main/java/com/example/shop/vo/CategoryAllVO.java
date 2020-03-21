package com.example.shop.vo;

import com.example.shop.model.Category;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Setter
public class CategoryAllVO {
    private List<CategoryPureVO> roots;
    private List<CategoryPureVO> subs;

    public CategoryAllVO(Map<String, List<Category>> map) {
        List<Category> roots = map.get("roots");
        List<Category> subs = map.get("subs");

        this.roots = roots.stream().map(CategoryPureVO::new).collect(Collectors.toList());
        this.subs = subs.stream().map(CategoryPureVO::new).collect(Collectors.toList());
    }
}
