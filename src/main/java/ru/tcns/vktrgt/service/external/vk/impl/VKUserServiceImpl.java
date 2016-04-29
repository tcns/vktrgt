package ru.tcns.vktrgt.service.external.vk.impl;

import com.sun.org.apache.bcel.internal.generic.LSTORE;
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

    @Override
    public FriendsResponse getUserFriends(Integer userId) {
        try {
            String url = FRIENDS_PREFIX + "get?user_id=" + userId;
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
    public List<User> getUserRelatives(List<Integer> userIds) {
        try {
            String fields = "relation,relatives";
            String url = USERS_PREFIX + "get?user_ids=" + ArrayUtils.getDelimetedList(userIds)+"&fields="+fields;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            List<User> response = VKResponseParser.parseUsersResponse(ans);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    @Override
    public CommonIDResponse getUserFriendIds(Integer userId) {
        try {
            String url = FRIENDS_PREFIX + "get?user_id=" + userId+"&order=id";
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
    public Map<Integer, Integer> intersectSubscriptions(List<Integer> users, Integer min) {
        Map<Integer, Integer> result = new HashMap<>();
        ArrayUtils utils = new ArrayUtils();
        for (int i = 0; i < users.size(); i++) {
            SubscriptionsResponse cur = getSubscriptions("" + users.get(i));
            List<Integer> curResult = cur.getGroups();
            result = utils.intersectWithCount(result, curResult);
        }
        return ArrayUtils.sortByValue(result, min);
    }

    private CommonIDResponse getFollowers(int userId, int offset, int count) {
        try {
            String url = USERS_PREFIX + "getFollowers?user_id=" + userId + "&offset=" + offset
                + "&count=" + count;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommonIDResponse response = VKResponseParser.parseCommonResponseWithCount(ans);
            return response;
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CommonIDResponse();
    }
    @Override
    public List<Integer> getFollowers(Integer userId) {
        CommonIDResponse initial = getFollowers(userId, 0, 1);
        int count = initial.getCount();
        List<Integer> users = new ArrayList<>(count);
        for (int i = 0; i < count; i += 1000) {
            users.addAll(getFollowers(userId, i, 1000).getIds());
        }
        return users;
    }

    @Override
    public SubscriptionsResponse getSubscriptions(String userId) {
        try {
            String url = USERS_PREFIX + "getSubscriptions?user_id=" + userId;
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
    public Map<Integer, Integer> intersectUsers(List<Integer> users, Integer min) {
        Map<Integer, Integer> result = new HashMap<>();
        ArrayUtils utils = new ArrayUtils();
        for (int i = 0; i < users.size(); i++) {
            CommonIDResponse cur = getUserFriendIds(users.get(i));
            List<Integer> curResult = cur.getIds();
            result = utils.intersectWithCount(result, curResult);
        }
        return ArrayUtils.sortByValue(result, min);
    }
}
