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
import ru.tcns.vktrgt.api.vk.Users;

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

    @RequestMapping(value = "/users/intersect",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Map<Long, Integer>> intersectUsers(@RequestParam List<Long> ids,
    @RequestParam Integer min) throws URISyntaxException {
        Map<Long, Integer> userIds = Users.intersectUsers(ids, min);
        return ResponseEntity.ok(userIds);
    }
}
