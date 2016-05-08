package ru.tcns.vktrgt.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.security.SecurityUtils;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import java.io.File;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by TIMUR on 02.05.2016.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {
    @Inject
    UserTaskRepository repository;
    @Inject
    UserService userService;
    @Inject
    ExportService exportService;


    @RequestMapping(value = "/tasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserTask>> getTasks(Pageable pageable) throws URISyntaxException {
        Page<UserTask> groups = repository.findByUserId(userService.getUserWithAuthorities().getId(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(groups, "/api/tasks");
        List<UserTask> tasks = groups.getContent();
        tasks.parallelStream().forEach(userTask -> userTask.setPayload(""));
        return new ResponseEntity<>(tasks.parallelStream().collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/tasks/export/txt",
        method = RequestMethod.GET,
        produces = "text/plain")
    @Timed
    @ResponseBody
    public FileSystemResource exportTaskToTxt(@RequestParam String taskId,
                                              @RequestParam String fileName) throws URISyntaxException {
        UserTask task = repository.findOne(taskId);
        if (task == null) {
            return null;
        }
        if (!userService.getUserWithAuthorities().getId().equals(task.getUserId())) {
            return null;
        }
        File f = exportService.getFileCSVFromJson(task.getPayload(), fileName);
        FileSystemResource resource = new FileSystemResource(f);
        return resource;
    }
}
