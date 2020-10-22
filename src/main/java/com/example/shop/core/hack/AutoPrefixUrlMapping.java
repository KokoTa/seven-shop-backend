package com.example.shop.core.hack;

import org.springframework.beans.factory.annotation.Value;
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
                // 用新的请求对象替换旧的请求对象
                // 这个新的请求对象增加了路径前缀
                // 比如原本的请求是 /banner，由于新增加了路径前缀 /v1，现在变成了 /v1/banner
                RequestMappingInfo newRequestMappingInfo = RequestMappingInfo.paths(prefix).build()
                        .combine(mappingInfo);
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
