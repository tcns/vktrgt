package ru.tcns.vktrgt.service.export.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.tcns.vktrgt.domain.util.parser.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TIMUR on 08.05.2016.
 */
@Service
public class ExportService {
    public InputStream getStreamFromJson(String json) {
        return IOUtils.toInputStream(json, StandardCharsets.UTF_8);
    }
    public InputStream getStreamFromObject(Object o) {
        return IOUtils.toInputStream(JsonParser.objectToJson(o), StandardCharsets.UTF_8);
    }
    public InputStream getStreamFromObject(String o) {
        if (null == o) {
            o = StringUtils.EMPTY;
        }
        return IOUtils.toInputStream(o, StandardCharsets.UTF_8);
    }
    public String getCSV(String jsonArray) {
        JSONArray array = new JSONArray(jsonArray);


        String csv = CDL.toString(array);
        return csv;
    }
    public List<String> getListOfStrings(MultipartFile file, String separator) {
        List<String> stringList = new ArrayList<>();
        if (file==null) {
            return stringList;
        }
        try {
            String s = new String(file.getBytes(), StandardCharsets.UTF_8);
            String[] result = s.split(separator);
            stringList = new ArrayList<>(result.length);
            for (String r: result) {
                r = r.trim();
                if (!r.isEmpty()) {
                    stringList.add(r);
                }
            }
            return stringList;
        } catch (IOException e) {
            return stringList;
        }


    }
}
