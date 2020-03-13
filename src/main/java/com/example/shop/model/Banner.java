package com.example.shop.model;

import javax.persistence.*;

@Entity
@Table(name = "banner")
public class Banner {

    @Id
    private long id;
    @Column(length = 30)
    private String name;
    private String title;
    private String description;
    private String img;

    @Transient
    private String silent;
}
