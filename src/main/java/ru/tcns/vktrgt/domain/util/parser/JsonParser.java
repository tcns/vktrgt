package ru.tcns.vktrgt.domain.util.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by TIMUR on 02.05.2016.
 */
public class JsonParser {

    public static String objectToJson(Object object) {
        if (object == null) {
            return "";
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            return "";
        }
    }
}
