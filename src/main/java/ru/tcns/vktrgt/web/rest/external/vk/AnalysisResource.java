package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;

import javax.inject.Inject;
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

    @RequestMapping(value = "/analyse",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> analyseUsers(@RequestBody AnalyseDTO analyseDTO) throws URISyntaxException {
        analysisService.analyseUsers(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            analyseDTO.getTaskInfo()), analyseDTO.getUsers(), analyseDTO);
        return ResponseEntity.ok().build();
    }
}
