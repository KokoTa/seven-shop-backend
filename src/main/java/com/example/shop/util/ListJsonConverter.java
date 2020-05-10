package com.example.shop.util;

import com.example.shop.exception.http.JsonConvertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.AttributeConverter;
import java.util.List;

public class ListJsonConverter implements AttributeConverter<List<Object>, String> {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<Object> objects) {
        try {
            return objectMapper.writeValueAsString(objects);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonConvertException(90001);
        }
    }

    @Override
    public List<Object> convertToEntityAttribute(String s) {
        try {
            if (s == null)
                return null;
            return objectMapper.readValue(s, List.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonConvertException(90001);
        }
    }
}
