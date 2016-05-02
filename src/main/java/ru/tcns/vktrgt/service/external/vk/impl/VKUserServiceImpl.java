package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.FriendsResponse;
import ru.tcns.vktrgt.domain.external.vk.response.SubscriptionsResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new FriendsResponse();
    }

    @Override
    public List<User> getUserInfo(List<String> userIds) {
        List<User> response = new ArrayList<>();
        String fields = "relation,relatives,domain,sex,bdate,country,city,home_town,contacts";
        List<String> users = ArrayUtils.getDelimetedLists(userIds, 1000);
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Future<List<User>>> tasks = new ArrayList<>();
        for (String user : users) {
            tasks.add(service.submit(() -> {
                String url = USERS_PREFIX + "get?user_ids=" + user + "&fields=" + fields + VERSION;
                Content content = Request.Get(url).execute().returnContent();
                String ans = content.asString();
                return VKResponseParser.parseUsersResponse(ans);
            }));
        }
        for (Future<List<User>> future : tasks) {
            try {
                response.addAll(future.get());
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        service.shutdown();
        return response;
    }

    @Override
    public CommonIDResponse getUserFriendIds(Integer userId) {
        try {
            String url = FRIENDS_PREFIX + "get?user_id=" + userId + "&order=id";
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommonIDResponse response = new ResponseParser<>(CommonIDResponse.class).parseResponseString(ans, RESPONSE_STRING);
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
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Future<List<Integer>>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i += 1000) {
            final Integer cur = i;
            tasks.add(service.submit(() -> getFollowers(userId, cur, 1000).getItems()));

        }
        for (Future<List<Integer>> task: tasks) {
            try {
                users.addAll(task.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
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
            List<Integer> curResult = cur.getItems();
            result = utils.intersectWithCount(result, curResult);
        }
        return ArrayUtils.sortByValue(result, min);
    }
}
