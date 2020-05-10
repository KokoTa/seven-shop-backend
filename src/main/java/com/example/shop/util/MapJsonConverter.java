package com.example.shop.util;

import com.example.shop.exception.http.JsonConvertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库数据格式转换器
 */
@Converter
public class MapJsonConverter implements AttributeConverter<Map<String, Object>, String> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Map<String, Object> stringObjectMap) {
        try {
            return objectMapper.writeValueAsString(stringObjectMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonConvertException(90001);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String s) {
        try {
            if (s == null)
                return null;
            return objectMapper.readValue(s, HashMap.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonConvertException(90001);
        }
    }
}
