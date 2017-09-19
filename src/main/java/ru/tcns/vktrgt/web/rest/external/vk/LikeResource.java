package ru.tcns.vktrgt.web.rest.external.vk;

import com.codahale.metrics.annotation.Timed;
import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.CreateCommentResponse;
import com.vk.api.sdk.queries.likes.LikesType;
import com.vk.api.sdk.queries.users.UserField;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.tcns.vktrgt.config.Constants;
import ru.tcns.vktrgt.domain.SocialUserConnection;
import ru.tcns.vktrgt.repository.SocialUserConnectionRepository;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.export.impl.ExportService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Created by tignatchenko on 20/02/17.
 */
@RestController
@RequestMapping("/api")
public class LikeResource {

    @Autowired
    SocialUserConnectionRepository connectionRepository;

    @Autowired
    UserService userService;

    @Autowired
    ExportService exportService;

    @RequestMapping(value = "/users/like",
                    method = RequestMethod.POST,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> doLike(@RequestParam List<String> userIds,
                                       @RequestParam String taskInfo,
                                       @RequestParam(required = false) MultipartFile file)
        throws ClientException, ApiException, InterruptedException {
        List<SocialUserConnection> map = connectionRepository.findAllByUserIdAndProviderIdOrderByRankAsc(userService.getUserWithAuthorities().getLogin(), "vkontakte");
        String token = map.get(0).getAccessToken();
        userIds.addAll(exportService.getListOfStrings(file, "\n"));
        UserActor actor = new UserActor(Integer.valueOf(Constants.VK_CLIENT_ID), token);
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);
        List<WallpostFull> post = vk.wall().get(actor).ownerId(Integer.parseInt(userIds.get(0))).count(1).offset(0).execute().getItems();
        if (!post.isEmpty()) {
            Date postDate = new Date(post.get(0).getDate() * 1000L);
            Calendar cal = Calendar.getInstance();
            cal.setTime(postDate);
            int pd = cal.get(Calendar.DAY_OF_YEAR);
            cal.setTime(new Date());
            int curd = cal.get(Calendar.DAY_OF_YEAR);
            if (pd==curd) {
                CreateCommentResponse response = vk.wall().createComment(actor, post.get(0).getId()).message("СВЕРШИЛОСЬ").execute();
                ResponseEntity.ok().build();
            }
        }

        return ResponseEntity.notFound().build();

    }

}
