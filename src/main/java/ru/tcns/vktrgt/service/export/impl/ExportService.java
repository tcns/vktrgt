package ru.tcns.vktrgt.service.export.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.CDL;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.util.parser.JsonParser;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
}
