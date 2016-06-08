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
import org.springframework.web.multipart.MultipartFile;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.google.impl.GoogleDriveImpl;
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
    @Inject
    GoogleDriveImpl googleDrive;
    @Inject
    ExportService exportService;

    @RequestMapping(value = "/users/leaders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> intersectUsersFromFriends(@RequestParam List<String> users,
                                                          @RequestParam Integer min, @RequestParam String taskInfo,
                                                          @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        users.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.intersectUsers(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), users, min);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> intersectGroupsFromUsers(@RequestParam List<String> users,
                                                         @RequestParam Integer min,
                                                         @RequestParam String taskInfo,
                                                         @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        users.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.intersectSubscriptions(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), users, min);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/info",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUsersInfo(@RequestParam List<String> userIds,
                                                   @RequestParam String taskInfo,
                                             @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        userIds.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.getUserInfo(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userIds);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/users/url",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUsersUrl(@RequestParam List<String> userIds,
                                             @RequestParam String taskInfo,
                                            @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        userIds.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.getUserURL(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userIds);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/urlid",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUsersId(@RequestParam List<String> userUrl,
                                            @RequestParam String taskInfo,
                                            @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        userUrl.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.getUserId(new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userUrl);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/followers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUserFollowers(@RequestParam Integer userId,
                                                 @RequestParam String taskInfo) throws URISyntaxException {
       vkUserService.getFollowers(new UserTaskSettings(userService.getUserWithAuthorities(), true,
           taskInfo, googleDrive), userId);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/users/vk",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<User>> searchUsersVk(@RequestParam String q,
                                                        @RequestParam String token) throws URISyntaxException {
        List<User> groups = vkUserService.searchUsersVK(q, token);
        return ResponseEntity.ok(groups);
    }

}
