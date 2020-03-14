package com.example.shop.model;

import javax.persistence.
        *;

@Entity
public class BannerItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String img;
    private String keyword;
    private Short type;
    private String name;
    private Long bannerId;
    @ManyToOne
    @JoinColumn(name = "bannerId", updatable = false, insertable = false, foreignKey = @ForeignKey( name = "none", value = ConstraintMode.NO_CONSTRAINT))
    private Banner banner;
}
