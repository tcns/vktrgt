package ru.tcns.vktrgt.api.vk;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;

import java.io.IOException;
import java.util.List;

/**
 * Created by TIMUR on 21.04.2016.
 */
public class Users {
    private static final String ACCESS_TOKEN = "&access_token=" + Common.getToken() + "&client_secret=" + Common.CLIENT_SECRET +
        "&v=5.50";
    public static Long reqTime = 0L;
    public static Long transTime = 0L;

    final static String URL_PREFIX = "https://api.vk.com/method/";
    final static String METHOD_PREFIX = "groups.";
    final static String PREFIX = URL_PREFIX + METHOD_PREFIX;

    public static GroupUserResponse getUserFriends(String userId, int offset, int count) {
        try {
            String url = PREFIX + "getMembers?group_id=" + groupId + "&offset=" + offset
                + "&count=" + count;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            GroupUserResponse response = VKResponseParser.parseGroupUserResponse(ans);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new GroupUserResponse();

    }

    public static GroupUsers getAllGroupUsers(String groupId) {
        GroupUserResponse initial = getGroupUsers(groupId, 0, 1);
        int count = initial.getCount();
        GroupUsers users = new GroupUsers(count, groupId);
        for (int i = 0; i < count; i += 1000) {
            users.append(getGroupUsers(groupId, i, 1000));
        }
        return users;
    }

    public static List<Long> intersectGroups(List<String> groups) {
        GroupUsers init = getAllGroupUsers(groups.get(0));
        ArrayUtils utils = new ArrayUtils();
        List<Long> result = init.getUsers();
        for (int i = 1; i < groups.size(); i++) {
            GroupUsers cur = getAllGroupUsers(groups.get(i));
            result = utils.intersect(result, cur.getUsers());
        }
        return result;
    }
}
