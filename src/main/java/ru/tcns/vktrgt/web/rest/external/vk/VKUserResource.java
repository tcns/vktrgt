package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.tcns.vktrgt.config.Constants;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.VKErrorCodes;
import ru.tcns.vktrgt.domain.external.vk.exception.VKException;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.google.impl.GoogleDriveImpl;
import ru.tcns.vktrgt.service.external.vk.intf.ActivityService;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;
import ru.tcns.vktrgt.web.rest.util.HeaderUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
    @Inject
    GroupService groupService;
    @Inject
    UserTaskRepository userTaskRepository;

    @RequestMapping(value = "/users/leaders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> intersectUsersFromFriends(@RequestParam List<String> users,
                                                          @RequestParam Integer min, @RequestParam String taskInfo,
                                                          @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        UserTask userTask = UserTask.create(VKUserService.LEADERS, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userTaskRepository);
        users.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.intersectUsers(userTask, users, min);
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
        UserTask userTask = UserTask.create(VKUserService.SUBSCRIPTIONS, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userTaskRepository);
        users.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.intersectSubscriptions(userTask, users, min);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/info",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUsersInfo(@RequestParam List<String> userIds,
                                                   @RequestParam String taskInfo,
                                             @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        UserTask userTask = UserTask.create(VKUserService.USER_INFO, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userTaskRepository);
        userIds.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.getUserInfo(userTask, userIds);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/users/url",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUsersUrl(@RequestParam List<String> userIds,
                                             @RequestParam String taskInfo,
                                            @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        UserTask userTask = UserTask.create(VKUserService.USER_URL, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userTaskRepository);
        userIds.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.getUserURL(userTask, userIds);
        return ResponseEntity.ok().build();
    }
    @RequestMapping(value = "/users/birth",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getNearestBirthdays(@RequestParam List<String> userIds,
                                                    @RequestParam List<String> relativeTypes,
                                                    @RequestParam List<Integer> genders,
                                                    @RequestParam Integer fromDays,
                                                    @RequestParam Integer nearestDays,
                                            @RequestParam String taskInfo,
                                            @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        UserTask userTask = UserTask.create(VKUserService.NEAREST_DATES, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userTaskRepository);
        userIds.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.searchNearestBirthdate(userTask, userIds, fromDays, nearestDays, relativeTypes, genders);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/urlid",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUsersId(@RequestParam List<String> userUrl,
                                            @RequestParam String taskInfo,
                                            @RequestParam(required = false) MultipartFile file) throws URISyntaxException {
        UserTask userTask = UserTask.create(VKUserService.USER_IDS, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userTaskRepository);
        userUrl.addAll(exportService.getListOfStrings(file, "\n"));
        vkUserService.getUserId(userTask, userUrl);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/users/followers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getUserFollowers(@RequestParam Integer userId,
                                                 @RequestParam String taskInfo) throws URISyntaxException {
        UserTask userTask = UserTask.create(VKUserService.FOLLOWERS, new UserTaskSettings(userService.getUserWithAuthorities(), true,
            taskInfo, googleDrive), userTaskRepository);
        vkUserService.getFollowers(userTask, userId);
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

    @RequestMapping(value = "/users/audio",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> searchUserAudioVk(@RequestParam List<String> users,
                                                  @RequestParam List<String> audios,
                                                  @RequestParam String taskInfo,
                                                  @RequestParam(required = false) MultipartFile file,
                                                  HttpServletRequest request) throws URISyntaxException {
        try {
            UserTask userTask = UserTask.create(VKUserService.AUDIO, new UserTaskSettings(userService.getUserWithAuthorities(), true,
                taskInfo, googleDrive), userTaskRepository);
            users.addAll(exportService.getListOfStrings(file, "\n"));
            groupService.searchVk("test", (String) request.getSession().getAttribute(Constants.VK_TOKEN));
            vkUserService.searchUserAudio(userTask,  users, audios,
                (String) request.getSession().getAttribute(Constants.VK_TOKEN));
        }  catch (VKException ex) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            if (ex.getVkErrorResponse().getErrorCode() == VKErrorCodes.UNAUTHORIZED) {
                status = HttpStatus.FORBIDDEN;
            }
            return ResponseEntity.status(status).headers(HeaderUtil.createVKErrorHeader(ex.getVkErrorResponse())).body(null);
        } catch (Exception ex) {
            HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
            HttpHeaders httpHeaders = new HttpHeaders();
            return ResponseEntity.status(status).headers(httpHeaders).body(null);
        }
        return ResponseEntity.ok().build();
    }


}
