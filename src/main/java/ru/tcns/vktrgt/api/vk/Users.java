package ru.tcns.vktrgt.api.vk;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.FriendsResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;

import java.io.IOException;
import java.util.*;

/**
 * Created by TIMUR on 21.04.2016.
 */
public class Users {
    private static final String ACCESS_TOKEN = "&access_token=" + Common.getToken() + "&client_secret=" + Common.CLIENT_SECRET +
        "&v=5.50";
    public static Long reqTime = 0L;
    public static Long transTime = 0L;

    final static String URL_PREFIX = "https://api.vk.com/method/";
    final static String FRIENDS_METHOD_PREFIX = "friends.";
    final static String USER_METHOD_PREFIX = "user.";
    final static String PREFIX = URL_PREFIX + FRIENDS_METHOD_PREFIX;

    public static FriendsResponse getUserFriends(Long userId) {
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
    public static CommonIDResponse getUserFriendIds(Long userId) {
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

    public static Map<Long, Integer> intersectUsers(List<Long> users, Integer min) {
        HashMap<Long, Integer> result = new HashMap<>();
        ArrayUtils utils = new ArrayUtils();
        for (int i = 0; i < users.size(); i++) {
            CommonIDResponse cur = getUserFriendIds(users.get(i));
            List<Long> curResult = cur.getIds();
            result = utils.intersectWithCount(result, curResult);
        }
        return ArrayUtils.sortByValue(result, min);
    }
}
