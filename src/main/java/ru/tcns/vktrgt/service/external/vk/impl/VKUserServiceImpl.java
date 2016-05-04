package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.FriendsResponse;
import ru.tcns.vktrgt.domain.external.vk.response.SubscriptionsResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by TIMUR on 21.04.2016.
 */
@Service
public class VKUserServiceImpl extends AbstractVKUserService {
    public static final String BEAN_NAME = "VKUserServiceImpl";
    public static final String USER_INFO = BEAN_NAME + "userInfo";
    public static final String FOLLOWERS = BEAN_NAME + "followers";
    public static final String SUBSCRIPTIONS = BEAN_NAME + "subscriptions";
    public static final String USERS = BEAN_NAME + "users";
    public static final String USER_URL = BEAN_NAME + "userUrl";

    @Inject
    UserTaskRepository repository;

    @Override
    public Future<List<String>> getUserURL(UserTaskSettings settings, List<String> userIds) {
        List<String> response = new ArrayList<>();
        UserTask userTask = new UserTask(USER_URL, settings, repository);
        userTask = userTask.saveInitial(userIds.size());
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
                List<User> list = future.get();
                List<String> stringList = list.parallelStream().map(a -> a.getId() + ": " + DOMAIN_PREFIX + a.getDomain())
                    .collect(Collectors.toList());
                userTask = userTask.saveProgress(list.size());
                response.addAll(stringList);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        service.shutdown();
        userTask.saveFinal(response);
        return new AsyncResult<>(response);
    }

    @Override
    @Async
    public Future<List<User>> getUserInfo(UserTaskSettings settings, List<String> userIds) {
        List<User> response = new ArrayList<>();
        UserTask userTask = new UserTask(USER_INFO, settings, repository);
        userTask = userTask.saveInitial(userIds.size());
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
                List<User> list = future.get();
                userTask = userTask.saveProgress(list.size());
                response.addAll(list);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        service.shutdown();
        userTask.saveFinal(response);
        return new AsyncResult<>(response);
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
    @Async
    public Future<Map<Integer, Integer>> intersectSubscriptions(UserTaskSettings settings, List<Integer> users, Integer min) {
        Map<Integer, Integer> result = new HashMap<>();
        UserTask userTask = new UserTask(SUBSCRIPTIONS, settings, repository);
        userTask  = userTask.saveInitial(users.size());
        ArrayUtils utils = new ArrayUtils();

        for (int i = 0; i < users.size(); i++) {
            SubscriptionsResponse cur = getSubscriptions("" + users.get(i));
            List<Integer> curResult = cur.getGroups();
            result = utils.intersectWithCount(result, curResult);
            userTask = userTask.saveProgress(1);
        }
        Map<Integer, Integer> response = ArrayUtils.sortByValue(result, min);
        userTask.saveFinal(response);
        return new AsyncResult<>(response);
    }



    @Override
    @Async
    public Future<List<Integer>> getFollowers(UserTaskSettings settings, Integer userId) {
        CommonIDResponse initial = getFollowers(userId, 0, 1);
        int count = initial.getCount();
        UserTask userTask = new UserTask(FOLLOWERS, settings, repository);
        userTask = userTask.saveInitial(count);
        List<Integer> users = new ArrayList<>(count);
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Future<List<Integer>>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i += 1000) {
            final Integer cur = i;
            tasks.add(service.submit(() -> getFollowers(userId, cur, 1000).getItems()));

        }
        for (Future<List<Integer>> task: tasks) {
            try {
                List<Integer> list = task.get();
                userTask = userTask.saveProgress(list.size());
                users.addAll(list);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        userTask.saveFinal(users);
        service.shutdown();
        return new AsyncResult<>(users);
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
    @Async
    public Future<Map<Integer, Integer>> intersectUsers(UserTaskSettings settings, List<Integer> users, Integer min) {
        Map<Integer, Integer> result = new HashMap<>();
        UserTask userTask = new UserTask(USERS, settings, repository);
        userTask = userTask.saveInitial(users.size());
        ArrayUtils utils = new ArrayUtils();
        for (int i = 0; i < users.size(); i++) {
            CommonIDResponse cur = getUserFriendIds(users.get(i));
            List<Integer> curResult = cur.getItems();
            result = utils.intersectWithCount(result, curResult);
            userTask = userTask.saveProgress(1);
        }
        Map<Integer, Integer> response = ArrayUtils.sortByValue(result, min);
        userTask.saveFinal(response);
        return new AsyncResult<>(response);
    }
}
