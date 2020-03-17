package com.example.shop.vo;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * 把查询数据库得到的 Page 对象转为输出到前端的 Paging 对象
 * @param <T> 源数据类型
 * @param <K> 目标数据类型
 */
public class PagingDozer<T, K> extends Paging {

    @SuppressWarnings("unchecked")
    public PagingDozer(Page<T> pageT, Class<K> kClass) {
        this.initPageParameter(pageT);

        List<T> tList = pageT.getContent();
        List<K> kList = new ArrayList<>();
        Mapper mapper = DozerBeanMapperBuilder.buildDefault();

        tList.forEach(t -> {
            K k = mapper.map(t, kClass);
            kList.add(k);
        });

        this.setItems(kList);
    }
}
