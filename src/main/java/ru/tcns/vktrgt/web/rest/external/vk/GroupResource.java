package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.repository.external.vk.GroupIdRepository;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.google.impl.GoogleDriveImpl;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;
import ru.tcns.vktrgt.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by TIMUR on 13.04.2016.
 */
@RestController
@RequestMapping("/api")
public class GroupResource {
    private final Logger log = LoggerFactory.getLogger(GroupResource.class);

    @Inject
    private GroupService groupService;
    @Inject
    UserService userService;
    @Inject
    GoogleDriveImpl googleDrive;
    @Inject
    ExportService exportService;

    @RequestMapping(value = "/groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> collectGroups(@RequestParam Integer from,
                                              @RequestParam Integer to,
                                              @RequestParam Boolean saveIds,
                                              @RequestParam Boolean useIds) throws URISyntaxException {
        log.debug("REST Attempt to search groups");
        groupService.getGroupInfoById(from, to, saveIds, useIds);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/groups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Group>> getAllGroups(Pageable pageable) throws URISyntaxException {
        Page<Group> groups = groupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(groups, "/api/groups");
        return new ResponseEntity<>(groups.getContent().stream()
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }
    @RequestMapping(value = "/groups/vk",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Group>> searchGroupsVk(@RequestParam String q,
                                                        @RequestParam String token) throws URISyntaxException {
        List<Group> groups = groupService.searchVk(q, token);
        return ResponseEntity.ok(groups);
    }
    @RequestMapping(value = "/groups/info/vk",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> searchGroupsInfoVk(@RequestParam List<String> names,
                                                      @RequestParam String taskInfo,
                                                   @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        names.addAll(exportService.getListOfStrings(file, "\n"));
        groupService.getGroupsInfo(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), names);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/groups/search/name",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Group>> getSearchGroupsByName(@RequestParam String name, @RequestParam Boolean restrict,
                                                             @RequestParam int page, @RequestParam int size) throws URISyntaxException {
        Pageable pageable = new PageRequest(page,size);
        Page<Group> groups = groupService.searchByName(name, restrict, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(groups, "/api/groups");
        return new ResponseEntity<>(groups.getContent().stream()
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/groups/search/ids",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Group>> getSearchGroupsByNames(@RequestParam List<String> names) throws URISyntaxException {
        List<Group> groups = groupService.searchByNames(names);
        return ResponseEntity.ok(groups);
    }

    @RequestMapping(value = "/groups/users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> intersectUsersFromGroups(@RequestParam List<String> names,
                                                                  @RequestParam String taskInfo,
                                                         @RequestParam Integer minCount,
                                                         @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        names.addAll(exportService.getListOfStrings(file, "\n"));
        groupService.intersectGroups(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), names, minCount);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/groups/members",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getGroupMembers(@RequestParam String groupId,
                                                         @RequestParam String taskInfo) throws URISyntaxException {

        groupService.getAllGroupUsers(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), groupId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/groups",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAllGroups() throws URISyntaxException {
        groupService.deleteAll();
        return ResponseEntity.ok().build();
    }
}
