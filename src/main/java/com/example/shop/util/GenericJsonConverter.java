package com.example.shop.util;

import com.example.shop.exception.http.JsonConvertException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenericJsonConverter {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setObjectMapper(ObjectMapper objectMapper) {
        GenericJsonConverter.objectMapper = objectMapper;
    }

    public static <T> String objectToJson(T o) {
        try {
            return GenericJsonConverter.objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonConvertException(90001);
        }
    }

    public  static <T> T jsonToObject(String s, TypeReference<T> tr) {
        try {
            if (s == null) return null;
            return GenericJsonConverter.objectMapper.readValue(s, tr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new JsonConvertException(90001);
        }
    }
}
