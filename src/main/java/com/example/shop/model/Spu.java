package com.example.shop.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Spu {
    @Id
    private Long id;
    private String title;
    private String subTitle;

    @ManyToMany(mappedBy = "spuList")
    private List<Theme> themeList;
}
