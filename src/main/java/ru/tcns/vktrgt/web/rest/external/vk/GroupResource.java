package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.api.vk.Groups;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;
import ru.tcns.vktrgt.web.rest.dto.BlogDTO;
import ru.tcns.vktrgt.web.rest.util.HeaderUtil;
import ru.tcns.vktrgt.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
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

    @RequestMapping(value = "/groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> collectGroups() throws URISyntaxException {
        log.debug("REST Attempt to search groups");
        List<Group>  groups = Groups.getGroupInfoById(ArrayUtils.getDelimetedLists(1,100,500));
        groupService.saveAll(groups);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/groups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Group>> getAllGroups() throws URISyntaxException {
        List<Group> groups = groupService.findAll();
        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}
