package com.example.shop.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "banner")
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 30)
    private String name;
    private String title;
    private String description;
    private String img;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "banner")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<BannerItem> items;
}
