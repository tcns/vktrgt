package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.external.vk.intf.ActivityService;
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/activity",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getActiveAuditory(@RequestBody ActiveAuditoryDTO activeAuditoryDTO) throws URISyntaxException {
        activityService.getActiveAuditory(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            activeAuditoryDTO.getTaskInfo()), activeAuditoryDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/topics",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getActiveTopicAuditory(@RequestParam List<String> topicUrls,
                                                       @RequestParam Integer minCount,
                                                       @RequestParam String taskInfo) throws URISyntaxException {
        activityService.getActiveTopicAuditory(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo), topicUrls, minCount);
        return ResponseEntity.ok().build();
    }
}
