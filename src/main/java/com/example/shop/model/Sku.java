package com.example.shop.model;

import com.example.shop.util.GenericJsonConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Where(clause = "delete_time is null and online = 1")
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

//    JSON 转换方式 1
//    @Convert(converter = ListJsonConverter.class)
//    private List<Spec> specs;
//    @Convert(converter = MapJsonConverter.class)
//    private Map<String, Object> test;

//    JSON 转换方式 2
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

    public BigDecimal getActualPrice() {
        return discountPrice == null ? price : discountPrice;
    }

    @JsonIgnore
    public List<String> getSpecValueList() {
        return getSpecs().stream()
                .map(Spec::getValue)
                .collect(Collectors.toList());
    }
}
