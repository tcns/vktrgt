package ru.tcns.vktrgt.service.export.impl;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Created by TIMUR on 08.05.2016.
 */
@Service
public class ExportService {
    public File getFileCSVFromJson(String json, String fileName) {
        File f  = new File(fileName+".txt");
        try {
            FileUtils.writeStringToFile(f, json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
}
