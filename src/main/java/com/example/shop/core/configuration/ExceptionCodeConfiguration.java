package com.example.shop.core.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@PropertySource(value = "classpath:config/exception-code.properties")
@ConfigurationProperties(prefix = "exception")
@Getter
@Setter
public class ExceptionCodeConfiguration {
    private Map<Integer, String> codes = new HashMap<>();

    public String getMessage(int code) {
        return codes.get(code);
    }
}
