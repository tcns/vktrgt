package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;
import ru.tcns.vktrgt.domain.external.vk.response.CommentsResponse;
import ru.tcns.vktrgt.domain.external.vk.response.LikesResponse;
import ru.tcns.vktrgt.domain.external.vk.response.RepostResponse;
import ru.tcns.vktrgt.domain.external.vk.response.WallPostsResponse;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.UserService;
import ru.tcns.vktrgt.service.external.vk.intf.WallService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Date;

/**
 * Created by TIMUR on 03.05.2016.
 */
public abstract class AbstractWallService implements WallService {


    @Inject
    protected UserTaskRepository userTaskRepository;
    @Inject
    protected UserService userService;

    protected WallPostsResponse getWallPosts(int ownerId, int offset, int count) {
        try {
            String url = PREFIX + "get?owner_id=" + ownerId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            WallPostsResponse response = new ResponseParser<>(WallPostsResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new WallPostsResponse();
    }

    protected LikesResponse getLikes(Integer ownerId, String type, int wallId, int offset, int count) {
        try {
            String url = LIKES_PREFIX + "getList?owner_id=" + ownerId + "&item_id=" + wallId + "&type=" + type + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            LikesResponse response = new ResponseParser<>(LikesResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new LikesResponse();
    }

    protected CommentsResponse getTopicComments(Integer groupId, int topicId, int offset, int count) {
        try {
            String url = TOPIC_PREFIX + "getComments?group_id=" + Math.abs(groupId) + "&topic_id=" + topicId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommentsResponse response = new ResponseParser<>(CommentsResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CommentsResponse();
    }

    protected RepostResponse getReposts(Integer ownerId, int postId, int offset, int count) {
        try {
            String url = PREFIX + "getReposts?owner_id=" + ownerId + "&post_id=" + postId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            RepostResponse response = new ResponseParser<>(RepostResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new RepostResponse();
    }

    protected CommentsResponse getComments(Integer ownerId, int postId, int offset, int count) {
        try {
            String url = PREFIX + "getComments?owner_id=" + ownerId + "&post_id=" + postId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommentsResponse response = new ResponseParser<>(CommentsResponse.class).parseResponseString
                (ans, RESPONSE_STRING);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CommentsResponse();
    }


    protected boolean isBefore(Integer maxDays, WallPost post) {
        return DateUtils.parseUnixTime("" + post.getDate()).before(org.apache.commons.lang.time.DateUtils.addDays(new Date(), -maxDays));
    }
}
