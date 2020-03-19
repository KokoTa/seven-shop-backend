package com.example.shop.vo;

import com.example.shop.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Getter
@Setter
public class CategoryPureVo {
    private Long id;
    private String name;
    private String description;
    private Boolean isRoot;
    private Long parentId;
    private String img;
    private Long index;
    private Long online;
    private Long level;

    public CategoryPureVo(Category category) {
        BeanUtils.copyProperties(category, this);
    }
}
