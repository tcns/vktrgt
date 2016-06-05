package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang.math.IntRange;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.util.IntRangeDeserializer;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.google.impl.GoogleDriveImpl;
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by TIMUR on 04.05.2016.
 */
@RestController
@RequestMapping("/api")
public class AnalysisResource {

    @Inject
    private AnalysisService analysisService;
    @Inject
    UserService userService;
    @Inject
    GoogleDriveImpl googleDrive;
    @Inject
    ExportService exportService;

    @RequestMapping(value = "/analyse",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> analyseUsers(StandardMultipartHttpServletRequest request) throws URISyntaxException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule rangeModule = new SimpleModule();
        rangeModule.addKeyDeserializer(IntRange.class, IntRangeDeserializer.INSTANCE);
        mapper.registerModule(rangeModule);
        AnalyseDTO analyseDTO = mapper.readValue(request.getParameter("dto"), AnalyseDTO.class);
        MultipartFile file = request.getFile("file");
        analyseDTO.getUsers().addAll(exportService.getListOfStrings(file, "\n"));
        analysisService.analyseUsers(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            analyseDTO.getTaskInfo(), googleDrive), analyseDTO.getUsers(), analyseDTO);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/filter",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> filterUsers(StandardMultipartHttpServletRequest request) throws URISyntaxException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SimpleModule rangeModule = new SimpleModule();
        rangeModule.addKeyDeserializer(IntRange.class, IntRangeDeserializer.INSTANCE);
        mapper.registerModule(rangeModule);
        AnalyseDTO analyseDTO = mapper.readValue(request.getParameter("dto"), AnalyseDTO.class);
        MultipartFile file = request.getFile("file");
        analyseDTO.getUsers().addAll(exportService.getListOfStrings(file, "\n"));
        analysisService.filterUsers(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            analyseDTO.getTaskInfo(), googleDrive), analyseDTO.getUsers(), analyseDTO);
        return ResponseEntity.ok().build();
    }
}
