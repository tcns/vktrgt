package ru.tcns.vktrgt.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.security.SecurityUtils;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.web.rest.util.PaginationUtil;

import javax.inject.Inject;
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

    @RequestMapping(value = "/tasks",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<UserTask>> getTasks(Pageable pageable) throws URISyntaxException {
        Page<UserTask> groups = repository.findByUserId(userService.getUserWithAuthorities().getId(), pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(groups, "/api/tasks");
        return new ResponseEntity<>(groups.getContent().stream()
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
}
