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
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.UserService;
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
    @Inject
    UserService userService;

    @RequestMapping(value = "/users/leaders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> intersectUsersFromFriends(@RequestParam List<Integer> users,
                                                          @RequestParam Integer min, @RequestParam String taskInfo) throws URISyntaxException {
        vkUserService.intersectUsers(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo), users, min);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> intersectGroupsFromUsers(@RequestParam List<Integer> users,
                                                         @RequestParam Integer min,
                                                         @RequestParam String taskInfo) throws URISyntaxException {
        vkUserService.intersectSubscriptions(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo), users, min);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/info",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUsersInfo(@RequestParam List<String> userIds,
                                                   @RequestParam String taskInfo) throws URISyntaxException {
        vkUserService.getUserInfo(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo),userIds);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/followers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUserFollowers(@RequestParam Integer userId,
                                                 @RequestParam String taskInfo) throws URISyntaxException {
       vkUserService.getFollowers(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo),userId);
        return ResponseEntity.ok().build();
    }

}
