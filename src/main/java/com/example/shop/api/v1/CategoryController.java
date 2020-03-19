package com.example.shop.api.v1;

import com.example.shop.model.GridCategory;
import com.example.shop.service.CategoryService;
import com.example.shop.service.GridCategoryService;
import com.example.shop.vo.CategoryAllVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/category")
@Validated
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    GridCategoryService gridCategoryService;

    @GetMapping("/all")
    public CategoryAllVo getAllCategory() {
        return new CategoryAllVo(categoryService.getAllCategory());
    }

    @GetMapping("/grid/all")
    public List<GridCategory> getAllGridCategory() {
        return gridCategoryService.getAllGridCategory();
    }

}
