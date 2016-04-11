package ru.tcns.vktrgt.api.vk;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 3/18/16.
 */
public class Groups {
    private static final String ACCESS_TOKEN = "&access_token=" + Common.getToken() + "&client_secret=" + Common.CLIENT_SECRET +
        "&v=5.50";
    public static Long reqTime = 0L;
    public static Long transTime = 0L;

    final static String URL_PREFIX = "https://api.vk.com/method/";
    final static String METHOD_PREFIX = "groups.";
    final static String PREFIX = URL_PREFIX + METHOD_PREFIX;


    public static GroupUserResponse getGroupUsers(String groupId, int offset, int count) {
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

    public static List<Long> intersectGroups(String... groups) {
        GroupUsers init = getAllGroupUsers(groups[0]);
        ArrayUtils utils = new ArrayUtils();
        List<Long> result = init.getUsers();
        for (int i = 1; i < groups.length; i++) {
            GroupUsers cur = getAllGroupUsers(groups[i]);
            result = utils.intersect(result, cur.getUsers());
        }
        return result;
    }

    public static GroupResponse getGroups(String q, int count, int offset) {
        try {
            String url = PREFIX + "search?q=" + q + "&offset=" + offset
                + "&count=" + count + ACCESS_TOKEN;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();

            GroupResponse response = VKResponseParser.parseGroupSearchResponse(ans);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GroupResponse();
    }

    public static List<Group> getGroupInfoById(List<String> ids) {
        String fields = "city,country,place,description,wiki_page,members_count,counters,start_date,finish_date," +
            "public_date_label,activity,status,contacts,links,fixed_post,verified,site,main_album_id,main_section,market";

        List<Group> groups = new ArrayList<>();
        for (String id : ids) {
            Content content = null;
            try {
                String url = PREFIX + "getById?group_ids=" + id + "fields=" + fields;
                content = Request.Get(url).execute().returnContent();
                String ans = content.asString();
                groups.addAll(VKResponseParser.parseGroupGetByIdesponse(ans));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return groups;
    }
}
