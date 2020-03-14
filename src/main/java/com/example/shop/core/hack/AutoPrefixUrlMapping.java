package com.example.shop.core.hack;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.regex.Pattern;


public class AutoPrefixUrlMapping extends RequestMappingHandlerMapping {

    @Value("${condition.auto-prefix-api-package}")
    private String autoPrefixApiPackage;

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        RequestMappingInfo mappingInfo = super.getMappingForMethod(method, handlerType);
        if (mappingInfo != null) {
            String prefix = this.getPrefix(handlerType);
            if (prefix != null) {
                // path 前缀，build 构建一个新 info，combine 合并另一个 info
                RequestMappingInfo newRequestMappingInfo = RequestMappingInfo.paths(prefix).build().combine(mappingInfo);
                return newRequestMappingInfo;
            }
        }
        return mappingInfo;
    }

    // 返回类的包路径名
    public String getPrefix(Class<?> handlerType) {
        String packageName = handlerType.getPackage().getName();
        String pattern = this.autoPrefixApiPackage + ".*";

        // packageName 可能返回其他的包路径，要确保这个路径和 api 基础路径是匹配的
        if (Pattern.matches(pattern, packageName)) {
            String basePath = packageName.replace(autoPrefixApiPackage + ".", "/");
            String urlPath = basePath.replace("\\.", "/");

            return urlPath;
        }
        return null;
    }
}
