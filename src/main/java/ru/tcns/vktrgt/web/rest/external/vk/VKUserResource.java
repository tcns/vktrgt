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
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.VKErrorCodes;
import ru.tcns.vktrgt.domain.external.vk.exception.VKException;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.google.impl.GoogleDriveImpl;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;
import ru.tcns.vktrgt.web.rest.util.HeaderUtil;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
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

    @RequestMapping(value = "/users/audio",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> searchUserAudioVk(@RequestParam List<String> users,
                                                  @RequestParam List<String> audios,
                                                  @RequestParam String taskInfo,
                                                  @RequestParam(required = false) MultipartFile file,
                                                  HttpServletRequest request) throws URISyntaxException {
        try {
            users.addAll(exportService.getListOfStrings(file, "\n"));
            vkUserService.searchUserAudio(new UserTaskSettings(userService.getUserWithAuthorities(), true,
                    taskInfo, googleDrive),  users, audios,
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
