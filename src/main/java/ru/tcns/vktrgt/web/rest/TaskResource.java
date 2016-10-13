package ru.tcns.vktrgt.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
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
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;
import ru.tcns.vktrgt.web.rest.util.HeaderUtil;
import ru.tcns.vktrgt.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
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
    @RequestMapping(value = "/tasks/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> removeTask(@PathVariable String id) throws URISyntaxException {
        repository.delete(id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/tasks/export/txt",
        method = RequestMethod.GET,
        produces = "text/plain")
    @Timed
    @ResponseBody
    public void exportTaskToTxt(@RequestParam String taskId,
                                              @RequestParam String fileName,
                                              HttpServletResponse httpServletResponse) throws URISyntaxException {
        UserTask task = repository.findOne(taskId);
        if (task == null) {
            return;
        }
        if (!userService.getUserWithAuthorities().getId().equals(task.getUserId())) {
            return;
        }
        try {
            URL url = new URL(task.getPayload());
            InputStream stream = url.openStream();
            String ext = ".txt";
            if (task.getKind().equals(AnalysisService.FILTER_USERS)) {
                ext = ".csv";
            }
            byte[] fileNameBytes = fileName.getBytes("utf-8");
            String dispositionFileName = "";
            for (byte b: fileNameBytes) dispositionFileName += (char)(b & 0xff);
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename="+dispositionFileName+ext);
            IOUtils.copy(stream, httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @RequestMapping(value = "/tasks/export/content",
        method = RequestMethod.POST)
    @Timed
    public void exportContentToTxt(@RequestParam String content,
                                @RequestParam String fileName,
                                HttpServletResponse httpServletResponse) throws URISyntaxException {
        try {
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename="+fileName+".txt");
            httpServletResponse.getOutputStream().write(content.getBytes(Charsets.UTF_8));
            httpServletResponse.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
