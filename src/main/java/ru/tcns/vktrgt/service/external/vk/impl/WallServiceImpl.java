package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;
import ru.tcns.vktrgt.domain.external.vk.response.WallPostsResponse;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.service.external.vk.intf.WallService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by TIMUR on 29.04.2016.
 */
public class WallServiceImpl implements WallService {

    private WallPostsResponse getWallPosts(int wallId, int offset, int count) {
        try {
            String url = PREFIX + "get?owner_id=" + wallId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            WallPostsResponse response = new ResponseParser<WallPostsResponse>().parseResponseString
                (ans, RESPONSE_STRING, WallPostsResponse.class);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new WallPostsResponse();
    }

    @Override
    public List<WallPost> getWallPosts(Integer wallId, Integer maxDays) {
        List<WallPost> posts = new ArrayList<>();
        try {
            Integer count = getWallPosts(wallId, 0, 2).getCount();
            posts = new ArrayList<>(count);
            for (int i = 0; i < count; i += 100) {
                List<WallPost> cur = getWallPosts(wallId, i, i + 100).getItems();
                posts.addAll(cur);
                if (!cur.isEmpty()) {
                    WallPost post = cur.get(0);
                    if (isBefore(maxDays, post)) {
                        break;
                    }
                }
            }
            return posts;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return posts;
    }

    private boolean isBefore(Integer maxDays, WallPost post) {
        return DateUtils.parseUnixTime("" + post.getDate()).before(org.apache.commons.lang.time.DateUtils.addDays(new Date(), -maxDays));
    }
}
