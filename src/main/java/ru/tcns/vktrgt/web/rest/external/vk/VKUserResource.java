package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 22.04.2016.
 */
@RestController
@RequestMapping("/api")
public class VKUserResource {
    private final Logger log = LoggerFactory.getLogger(VKUserResource.class);
    @Inject
    VKUserService vkUserService;

    @RequestMapping(value = "/users/leaders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<Integer, Integer>> intersectUsersFromFriends(@RequestParam List<Integer> users,
    @RequestParam Integer min) throws URISyntaxException {
        Map<Integer, Integer> userIds = vkUserService.intersectUsers(users, min);
        return ResponseEntity.ok(userIds);
    }
    @RequestMapping(value = "/users/groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<Integer, Integer>> intersectGroupsFromUsers(@RequestParam List<Integer> users,
                                                                       @RequestParam Integer min) throws URISyntaxException {
        Map<Integer, Integer> userIds = vkUserService.intersectSubscriptions(users, min);
        return ResponseEntity.ok(userIds);
    }
    @RequestMapping(value = "/users/relatives",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<User>> intersectGroupsFromUsers(@RequestParam List<Integer> userIds) throws URISyntaxException {
        List<User> users = vkUserService.getUserRelatives(userIds);
        return ResponseEntity.ok(users);
    }
    @RequestMapping(value = "/users/followers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Integer>> getUserFollowers(@RequestParam Integer userId) throws URISyntaxException {
        List<Integer> userIds = vkUserService.getFollowers(userId);
        return ResponseEntity.ok(userIds);
    }

}
