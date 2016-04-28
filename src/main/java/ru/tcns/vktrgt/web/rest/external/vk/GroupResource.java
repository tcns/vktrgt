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
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupIds;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.repository.external.vk.GroupIdRepository;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;
import ru.tcns.vktrgt.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import java.net.URISyntaxException;
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
    private GroupIdRepository groupIdRepository;

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
    public ResponseEntity<List<Integer>> intersectUsersFromGroups(@RequestParam List<String> names) throws URISyntaxException {
        List<Integer> userIds = groupService.intersectGroups(names);
        return ResponseEntity.ok(userIds);
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
