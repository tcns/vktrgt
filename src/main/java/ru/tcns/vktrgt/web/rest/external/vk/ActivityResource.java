package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.google.impl.GoogleDriveImpl;
import ru.tcns.vktrgt.service.external.vk.intf.ActivityService;
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by TIMUR on 05.05.2016.
 */
@RestController
@RequestMapping("/api")
public class ActivityResource {
    @Inject
    private ActivityService activityService;
    @Inject
    UserService userService;
    @Inject
    GoogleDriveImpl googleDrive;
    @Inject
    ExportService exportService;
    @Inject
    UserTaskRepository userTaskRepository;

    @RequestMapping(value = "/activity",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getActiveAuditory(StandardMultipartHttpServletRequest request) throws URISyntaxException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        ActiveAuditoryDTO activeAuditoryDTO = mapper.readValue(request.getParameter("dto"), ActiveAuditoryDTO.class);
        MultipartFile file = request.getFile("file");
        activeAuditoryDTO.getGroups().addAll(exportService.getListOfStrings(file, "\n"));
        UserTask userTask = UserTask.create(ActivityService.ACTIVE_AUDITORY, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            activeAuditoryDTO.getTaskInfo(), googleDrive), userTaskRepository);
        activityService.getActiveAuditory(userTask, activeAuditoryDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/topics",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getActiveTopicAuditory(@RequestParam List<String> topicUrls,
                                                       @RequestParam Integer minCount,
                                                       @RequestParam String taskInfo,
                                                       @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        topicUrls.addAll(exportService.getListOfStrings(file, "\n"));
        UserTask userTask = UserTask.create(ActivityService.ACTIVE_TOPIC_AUDITORY, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userTaskRepository);
        activityService.getActiveTopicAuditory(userTask, topicUrls, minCount);
        return ResponseEntity.ok().build();
    }
}
