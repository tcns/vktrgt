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
import ru.tcns.vktrgt.api.vk.Groups;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupIds;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.repository.external.vk.GroupIdRepository;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;
import ru.tcns.vktrgt.web.rest.dto.BlogDTO;
import ru.tcns.vktrgt.web.rest.util.HeaderUtil;
import ru.tcns.vktrgt.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
        List<String> list;
        if (useIds) {
            List<GroupIds> idsList = groupIdRepository.findAll();
            list = ArrayUtils.getDelimetedLists(from, to, VKDicts.MAX_GROUP_REQUEST_COUNT, idsList);
        } else {
            list = ArrayUtils.getDelimetedLists(from, to, VKDicts.MAX_GROUP_REQUEST_COUNT);
        }
        for (String s : list) {
            List<Group> groups = Groups.getGroupInfoById(s);
            if (saveIds) {
                List<GroupIds> ids = groups.stream().map(p -> new GroupIds(p.getId().intValue())).collect(Collectors.toList());
                groupIdRepository.save(ids);
            }
            groupService.saveAll(groups);
        }
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
    @RequestMapping(value = "/groups/{name}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Group>> getSearchGroupsByName(@PathVariable String name, @RequestParam Boolean restrict,
                                                             @RequestParam int page, @RequestParam int size) throws URISyntaxException {
        Pageable pageable = new PageRequest(page,size);
        Page<Group> groups = groupService.searchByName(name, restrict, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(groups, "/api/groups");
        return new ResponseEntity<>(groups.getContent().stream()
            .collect(Collectors.toCollection(LinkedList::new)), headers, HttpStatus.OK);
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
