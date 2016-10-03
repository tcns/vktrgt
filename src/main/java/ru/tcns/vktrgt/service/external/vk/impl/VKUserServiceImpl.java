package ru.tcns.vktrgt.service.external.vk.impl;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.RelativeType;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.dict.VKErrorCodes;
import ru.tcns.vktrgt.domain.external.vk.exception.VKException;
import ru.tcns.vktrgt.domain.external.vk.internal.Audio;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.Relative;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.external.vk.response.*;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKUrlParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.export.impl.ExportService;

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

    @Inject
    UserTaskRepository repository;
    @Inject
    ExportService exportService;

    @Override
    @Async
    public Future<List<String>> getUserURL(UserTask userTask, List<String> userIds) {
        return new AsyncResult<>(getUserURLSync(userTask, userIds));
    }

    @Override
    @Async
    public Future<List<String>> getUserId(UserTask userTask, List<String> userUrls) {
        return new AsyncResult<>(getUserIdSync(userTask, userUrls));
    }

    @Override
    @Async
    public Future<List<User>> getUserInfo(UserTask userTask, List<String> userIds) {
        return new AsyncResult<>(getUserInfoSync(userTask, userIds));
    }

    @Override
    public List<User> searchUsersVK(String q, String token) {
        List<User> users = new ArrayList<>();
        Content content;
        try {
            String url = USERS_PREFIX + "search?q=" + q + "&count=1000&access_token=" + token + VERSION;
            content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            UserResponse response = new ResponseParser<>(UserResponse.class).parseResponseString(ans, RESPONSE_STRING);
            if (response != null) {
                users.addAll(response.getItems());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public CommonIDResponse getUserFriendIds(Integer userId) {
        try {
            String url = FRIENDS_PREFIX + "get?user_id=" + userId + "&order=id" + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommonIDResponse response = new ResponseParser<>(CommonIDResponse.class).parseResponseString(ans, RESPONSE_STRING);
            if (response != null) {
                return response;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new CommonIDResponse();

    }

    @Override
    @Async
    public Future<Map<Integer, Integer>> intersectSubscriptions(UserTask userTask, List<String> users, Integer min) {
        return new AsyncResult<>(intersectSubscriptionsSync(userTask, users, min));
    }


    @Override
    @Async
    public Future<Map<String, Integer>> searchUserAudio(UserTask userTask, List<String> users, List<String> audio, String token) throws VKException {
        return new AsyncResult<>(searchUserAudioSync(userTask, users, audio, token));
    }

    @Override
    @Async
    public Future<Set<String>> searchNearestBirthdate(UserTask userTask,
                                                      List<String> userIds,
                                                      Integer fromDays,
                                                      Integer nearestDays,
                                                      List<String> types,
                                                      List<Integer> gender) {
        return new AsyncResult<>(searchNearestBirthdateSync(userTask, userIds, fromDays, nearestDays,
            types, gender));
    }

    @Override
    public List<User> getUserInfoSync(UserTask userTask, List<String> userIds) {
        List<User> response = new ArrayList<>();
        userTask = userTask.startWork();
        List<String> convertedIds = userIds.parallelStream().map(a->VKUrlParser.getName(a)).collect(Collectors.toList());
        userTask = userTask.saveInitial(convertedIds.size());
        String fields = "relation,relatives,domain,sex,bdate,country,city,home_town,contacts";
        List<String> users = ArrayUtils.getDelimetedLists(convertedIds, 1000);
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
        userTask.saveFinal(exportService.getStreamFromObject(response));
        return response;
    }

    @Override
    public List<String> getUserURLSync(UserTask userTask, List<String> userIds) {
        List<String> response = new ArrayList<>();
        userTask = userTask.startWork();
        userTask = userTask.saveInitial(userIds.size());
        String fields = "domain";
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
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(response, "\n")));
        return response;
    }

    @Override
    public List<String> getUserIdSync(UserTask userTask, List<String> userUrls) {
        List<String> response = new ArrayList<>();
        userTask = userTask.startWork();
        userTask = userTask.saveInitial(userUrls.size());
        String fields = "domain";
        List<String> urls = userUrls.parallelStream().map(a -> VKUrlParser.getName(a)).collect(Collectors.toList());
        List<String> users = ArrayUtils.getDelimetedLists(urls, 1000);
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
                List<String> stringList = list.parallelStream().map(a -> "" + a.getId())
                    .collect(Collectors.toList());
                userTask = userTask.saveProgress(list.size());
                response.addAll(stringList);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        service.shutdown();
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(response, "\n")));
        return response;
    }

    @Override
    public Map<Integer, Integer> intersectUsersSync(UserTask userTask, List<String> users, Integer min) {
        Map<Integer, Integer> result = new HashMap<>();
        userTask = userTask.startWork();
        userTask = userTask.saveInitial(users.size());
        try {
            List<User> userList = getUserInfoSync(userTask.copyNoCreate(), users);
            users = new ArrayList<>(userList.size());
            for (User user: userList) {
                users.add(""+user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayUtils utils = new ArrayUtils();
        for (int i = 0; i < users.size(); i++) {
            Integer id = null;
            try {
                id = Integer.parseInt(users.get(i));
            } catch (NumberFormatException ex) {
                continue;
            }
            CommonIDResponse cur = getUserFriendIds(id);
            List<Integer> curResult = cur.getItems();
            if (curResult != null) {
                result = utils.intersectWithCount(result, curResult);
                userTask = userTask.saveProgress(1);
            } else {
                userTask = userTask.saveError("Не удалось получить от пользователя с id " + users.get(i));
            }
        }
        Map<Integer, Integer> response = ArrayUtils.sortByValue(result, min);
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(response.keySet(), "\n")));
        return response;
    }

    @Override
    public Map<Integer, Integer> intersectSubscriptionsSync(UserTask userTask, List<String> users, Integer min) {
        Map<Integer, Integer> result = new HashMap<>();
        userTask = userTask.startWork();
        try {
            List<User> userList = getUserInfoSync(userTask.copyNoCreate(), users);
            users = new ArrayList<>(userList.size());
            for (User user: userList) {
                users.add(""+user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userTask = userTask.saveInitial(users.size());
        ArrayUtils utils = new ArrayUtils();

        for (int i = 0; i < users.size(); i++) {
            SubscriptionsResponse cur = getSubscriptions(users.get(i));
            if (cur != null && cur.getGroups() != null) {
                List<Integer> curResult = cur.getGroups();
                result = utils.intersectWithCount(result, curResult);
            }
            userTask = userTask.saveProgress(1);
        }
        Map<Integer, Integer> response = ArrayUtils.sortByValue(result, min);
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(response.keySet(), "\n")));
        return response;
    }

    @Override
    public List<Integer> getFollowersSync(UserTask userTask, Integer userId) {
        CommonIDResponse initial = getFollowers(userId, 0, 1);
        int count = initial.getCount();
        userTask = userTask.startWork();
        userTask = userTask.saveInitial(count);
        List<Integer> users = new ArrayList<>(count);
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Future<List<Integer>>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i += 1000) {
            final Integer cur = i;
            tasks.add(service.submit(() -> getFollowers(userId, cur, 1000).getItems()));

        }
        for (Future<List<Integer>> task : tasks) {
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
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(users, "\n")));
        service.shutdown();
        return users;
    }

    @Override
    public Map<String, Integer> searchUserAudioSync(UserTask userTask, List<String> users, List<String> audio, String token) throws VKException {
        Map<String, Integer> result = new HashMap<>();
        userTask = userTask.startWork();
        try {
            List<User> userList = getUserInfoSync(userTask.copyNoCreate(), users);
            users = new ArrayList<>(userList.size());
            for (User user: userList) {
                users.add(""+user.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        userTask = userTask.saveInitial(users.size());
        for (int i = 0; i < users.size(); i++) {
            try {
                Thread.currentThread().sleep(1000);
                if (i>0 && i%50==0) {
                    Thread.currentThread().sleep(10000);
                }
                AudioResponse cur = getUserAudio(users.get(i), token);
                if (cur != null && cur.getItems() != null) {
                    int count = 0;
                    for (Audio a: cur.getItems()) {
                        for (String audioToSearch: audio) {
                            if (a.getTitle().length() > audioToSearch.length()) {
                                if (a.getArtist().toLowerCase().contains(audioToSearch.toLowerCase())) {
                                    count++;
                                }
                            } else {
                                if (audioToSearch.toLowerCase().contains(a.getArtist().toLowerCase())) {
                                    count++;
                                }
                            }
                        }
                    }
                    result.put(users.get(i), count);
                }
            } catch (VKException ex) {
                if (ex.getVkErrorResponse().getErrorCode()== VKErrorCodes.UNAUTHORIZED) {
                    throw new VKException(ex.getVkErrorResponse());
                }
                if (ex.getVkErrorResponse().getErrorCode()==VKErrorCodes.CAPTCHA_NEEDED) {
                    try {
                        Thread.currentThread().sleep(10000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            userTask = userTask.saveProgress(1);
        }
        Map<String, Integer> response = ArrayUtils.sortByValue(result, 0);
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(response.keySet(), "\n")));
        return response;
    }

    @Override
    public Set<String> searchNearestBirthdateSync(UserTask userTask, List<String> userIds, Integer fromDays, Integer nearestDays, List<String> types, List<Integer> gender) {
        int batchSize = 20;
        userTask = userTask.startWork();
        List<List<String>> batches = Lists.partition(userIds, batchSize);
        userTask = userTask.saveInitial(userIds.size());
        Set<String> response = new HashSet<>();
        for (List<String> batch: batches) {
            try {
                List<User> userList = getUserInfoSync(userTask.copyNoCreate(), batch);
                for (User user: userList) {
                    List<Relative> relatives = user.getRelatives();
                    List<String> idsToFind = new ArrayList<>();
                    if (types.contains(RelativeType.PARTNER)) {
                        User relationPartner = user.getRelationPartner();
                        if (relationPartner!=null && relationPartner.getId()!=null) {
                            idsToFind.add(""+relationPartner.getId());
                        }
                    }
                    if (relatives != null)   {
                        for (Relative relative: relatives) {
                            if (relative.getId() != null && types.contains(relative.getType())) {
                                idsToFind.add(""+relative.getId());
                            }
                        }
                    }
                    if (idsToFind.size() > 0) {
                        List<User> relativeUserAnswer = getUserInfoSync(userTask.copyNoCreate(), idsToFind);
                        for (User relativeUser: relativeUserAnswer) {
                            if (relativeUser.getBdate() != null) {
                                Date birthDay = org.apache.commons.lang3.time.DateUtils.
                                    parseDate(relativeUser.getBdate(), VKDicts.BDAY_FORMATS);
                                DateTime dt = new DateTime(birthDay);
                                dt.dayOfYear().get();
                                int diff = new DateTime(birthDay).dayOfYear().get() - DateTime.now().dayOfYear().get();
                                if (diff < 0) {
                                    diff += 365;
                                }
                                if (diff <= nearestDays && diff >= fromDays) {
                                    if (gender != null && gender.size() > 0) {
                                        if (gender.contains(relativeUser.getSex())) {
                                            response.add("" + user.getId());
                                        }
                                    } else {
                                        response.add("" + user.getId());
                                    }
                                }
                            }
                        }
                    }

                    userTask = userTask.saveProgress(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(response, "\n")));
        return response;
    }

    @Override
    @Async
    public Future<List<Integer>> getFollowers(UserTask userTask, Integer userId) {
        return new AsyncResult<>(getFollowersSync(userTask, userId));
    }

    @Override
    public SubscriptionsResponse getSubscriptions(String userId) {
        try {
            String url = USERS_PREFIX + "getSubscriptions?user_id=" + userId;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            SubscriptionsResponse response = VKResponseParser.parseUserSubscriptions(ans);
            if (response != null) {
                return response;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new SubscriptionsResponse();
    }

    @Override
    @Async
    public Future<Map<Integer, Integer>> intersectUsers(UserTask userTask, List<String> users, Integer min) {
        return new AsyncResult<>(intersectUsersSync(userTask, users, min));
    }
}
