package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.security.SecurityUtils;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.external.vk.intf.WallService;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 02.05.2016.
 */
@RestController
@RequestMapping("/api")
public class WallResource {

    @Inject
    WallService wallService;

    @Inject
    UserService userService;

    @RequestMapping(value = "/wall/leaders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> getTopicCommentsWithLikes(@RequestParam Integer ownerId,
                                                          @RequestParam Integer postId,
                                                          @RequestParam String taskInfo) throws URISyntaxException {
        UserTaskSettings settings = new UserTaskSettings(userService.getUserWithAuthorities(), true, taskInfo);
        wallService.getTopicCommentsWithLikes(settings, ownerId, postId);
        return ResponseEntity.ok().build();
    }
}
