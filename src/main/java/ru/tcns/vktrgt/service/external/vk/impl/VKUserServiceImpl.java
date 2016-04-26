package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.api.vk.Common;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.FriendsResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;
import ru.tcns.vktrgt.domain.external.vk.response.SubscriptionsResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import java.io.IOException;
import java.util.*;

/**
 * Created by TIMUR on 21.04.2016.
 */
@Service
public class VKUserServiceImpl implements VKUserService {
    private static final String ACCESS_TOKEN = "&access_token=" + Common.getToken() + "&client_secret=" + Common.CLIENT_SECRET +
        "&v=5.50";
    public static Long reqTime = 0L;
    public static Long transTime = 0L;

    final static String URL_PREFIX = "https://api.vk.com/method/";
    final static String FRIENDS_METHOD_PREFIX = "friends.";
    final static String USER_METHOD_PREFIX = "users.";
    final static String PREFIX = URL_PREFIX + FRIENDS_METHOD_PREFIX;

    @Override
    public FriendsResponse getUserFriends(Long userId) {
        try {
            String url = PREFIX + "get?user_id=" + userId;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            FriendsResponse response = VKResponseParser.parseFriendsResponse(ans);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new FriendsResponse();

    }
    @Override
    public CommonIDResponse getUserFriendIds(Long userId) {
        try {
            String url = PREFIX + "get?user_id=" + userId+"&order=id";
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommonIDResponse response = VKResponseParser.parseCommonIDResponse(ans);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CommonIDResponse();

    }

    @Override
    public Map<Long, Integer> intersectSubscriptions(List<Long> users, Integer min) {
        Map<Long, Integer> result = new HashMap<>();
        ArrayUtils utils = new ArrayUtils();
        for (int i = 0; i < users.size(); i++) {
            SubscriptionsResponse cur = getSubscriptions("" + users.get(i));
            List<Long> curResult = cur.getGroups();
            result = utils.intersectWithCount(result, curResult);
        }
        return ArrayUtils.sortByValue(result, min);
    }

    @Override
    public SubscriptionsResponse getSubscriptions(String userId) {
        try {
            String url = URL_PREFIX + USER_METHOD_PREFIX + "getSubscriptions?user_id=" + userId;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            SubscriptionsResponse response = VKResponseParser.parseUserSubscriptions(ans);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new SubscriptionsResponse();
    }

    @Override
    public Map<Long, Integer> intersectUsers(List<Long> users, Integer min) {
        Map<Long, Integer> result = new HashMap<>();
        ArrayUtils utils = new ArrayUtils();
        for (int i = 0; i < users.size(); i++) {
            CommonIDResponse cur = getUserFriendIds(users.get(i));
            List<Long> curResult = cur.getIds();
            result = utils.intersectWithCount(result, curResult);
        }
        return ArrayUtils.sortByValue(result, min);
    }
}
