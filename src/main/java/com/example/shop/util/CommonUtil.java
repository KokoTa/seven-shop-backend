package com.example.shop.util;

import com.example.shop.bo.PageCounter;

public class CommonUtil {

    public static PageCounter convertToPageParameter(Integer start, Integer count) {
        Integer pageNo = start / count;
        return PageCounter.builder().pageNo(pageNo).pageSize(count).build();
    }
}
