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
        try {
            String[] vals = key.split("-");
            int min = Integer.parseInt(vals[0]);
            int max = Integer.parseInt(vals[1]);
            return new IntRange(min, max);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
