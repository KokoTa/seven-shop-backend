package com.example.shop.vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Paging<T> {
    private Integer pageSize; // 每页数据量
    private Integer pageNo; // 当前页
    private Long total; // 总数
    private Integer totalPage; // 总页
    private List<T> items; // 数据

    public Paging(Page<T> pageT) {
        this.initPageParameter(pageT);
        this.items = pageT.getContent();
    }

    void initPageParameter(Page<T> pageT) {
        this.pageSize = pageT.getSize();
        this.pageNo = pageT.getNumber();
        this.total = pageT.getTotalElements();
        this.totalPage = pageT.getTotalPages();
    }
}
