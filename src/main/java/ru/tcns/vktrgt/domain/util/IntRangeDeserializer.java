package ru.tcns.vktrgt.domain.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.KeyDeserializer;
import org.apache.commons.lang.math.IntRange;

import java.io.IOException;

/**
 * Created by TIMUR on 15.05.2016.
 */
public class IntRangeDeserializer extends KeyDeserializer {
    public static final IntRangeDeserializer INSTANCE = new IntRangeDeserializer();

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return null;
    }
}
