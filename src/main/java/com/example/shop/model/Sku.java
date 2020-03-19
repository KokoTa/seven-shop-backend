package com.example.shop.model;

import com.example.shop.util.GenericJsonConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
public class Sku extends BaseEntity {
    @Id
    private Long id;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private Boolean online;
    private String img;
    private String title;
    private Long spuId;
    private String code;
    private Long stock;
    private Long categoryId;
    private Long rootCategoryId;

//    @Convert(converter = ListJsonConverter.class)
//    private List<Spec> specs;
//    @Convert(converter = MapJsonConverter.class)
//    private Map<String, Object> test;

    private String specs;
    private String test;

    public List<Spec> getSpecs() {
        if (this.specs == null) {
            return null;
        } else {
            return GenericJsonConverter.jsonToObject(this.specs, new TypeReference<List<Spec>>() {
            });
        }
    }

    public void setSpecs(List<Spec> specs) {
        if (specs.isEmpty()) {
            this.specs = "";
        } else {
            this.specs = GenericJsonConverter.objectToJson(specs);
        }
    }

    public Map<String, Object> getTest() {
        if (this.test == null) {
            return null;
        } else {
            return GenericJsonConverter.jsonToObject(this.test, new TypeReference<Map<String, Object>>() {
            });
        }
    }

    public void setTest(Map<String, Object> test) {
        if (test == null) {
            this.test = "";
        } else {
            this.test = GenericJsonConverter.objectToJson(test);
        }
    }
}
