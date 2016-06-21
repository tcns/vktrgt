package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.internal.*;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKUrlParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by Тимур on 13.04.2016.
 */
@Service
public class GroupServiceImpl extends AbstractGroupService {

    public static final String BEAN_NAME = "GroupServiceImpl";
    public static final String ALL_USERS = BEAN_NAME + "AllUsers";
    public static final String INTERSECT_GROUPS = BEAN_NAME + "IntersectGroups";
    public static final String USER_GROUPS = BEAN_NAME + "UserGroups";
    public static final String GROUP_INFO = BEAN_NAME + "GroupInfo";
    @Inject
    UserTaskRepository repository;
    @Inject
    ExportService exportService;
    @Inject
    VKUserService userService;

    @Override
    public List<Group> searchVk(String q, String token) {
        List<Group> groups = new ArrayList<>();
        Content content = null;
        try {
            String url = PREFIX + "search?q=" + q + "&count=1000&access_token=" + token + VERSION;
            content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            GroupResponse groupResponse = new ResponseParser<>(GroupResponse.class).parseResponseString(ans, RESPONSE_STRING);
            if (groupResponse != null) {
                groups.addAll(groupResponse.getItems());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    @Async
    public Future<GroupUsers> getAllGroupUsers(UserTaskSettings settings, String groupId) {
        UserTask userTask = new UserTask(ALL_USERS, settings, repository);
        CommonIDResponse initial = getGroupUsers(groupId, 0, 1);
        if (initial == null || initial.getCount() == 0) {
            return new AsyncResult<>(new GroupUsers(0, groupId));
        }
        int count = initial.getCount();
        userTask = userTask.saveInitial(count);
        ExecutorService service = Executors.newFixedThreadPool(10);
        GroupUsers users = new GroupUsers(count, groupId);
        List<Future<CommonIDResponse>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i += 1000) {
            final int cur = i;
            try {
                tasks.add(service.submit(() -> getGroupUsers(VKUrlParser.getName(groupId), cur, 1000)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        for (Future<CommonIDResponse> f : tasks) {
            try {
                CommonIDResponse response = f.get();
                userTask = userTask.saveProgress(response.getItems().size());
                users.append(response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        service.shutdown();
        userTask.saveFinal(exportService.getStreamFromObject(users));
        return new AsyncResult<>(users);
    }

    @Override
    public Future<List<Group>> getGroupsInfo(UserTaskSettings settings, List<String> groups) {
        List<Group> response = new ArrayList<>();
        UserTask userTask = new UserTask(GROUP_INFO, settings, repository);
        List<String> convertedIds = groups.parallelStream().map(a->VKUrlParser.getName(a)).collect(Collectors.toList());
        List<String> groupsIds = ArrayUtils.getDelimetedLists(convertedIds, 1000);
        userTask = userTask.saveInitial(groupsIds.size());
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Future<List<Group>>> tasks = new ArrayList<>();
        for (String id : groupsIds) {
            tasks.add(service.submit(() -> getGroupInfoById(id)));
        }
        for (Future<List<Group>> future : tasks) {
            try {
                List<Group> list = future.get();
                userTask = userTask.saveProgress(list.size());
                response.addAll(list);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }
        service.shutdown();
        userTask.saveFinal(exportService.getStreamFromObject(response));
        return new AsyncResult<>(response);
    }

    @Override
    @Async
    public Future<Map<Integer, Integer>> intersectGroups(UserTaskSettings settings, List<String> groups, Integer minCount) {
        UserTask userTask = new UserTask(INTERSECT_GROUPS, settings, repository);
        ArrayUtils utils = new ArrayUtils();
        List<String> convertedIds = groups.parallelStream().map(a->VKUrlParser.getName(a)).collect(Collectors.toList());
        userTask = userTask.saveInitial(convertedIds.size());
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < convertedIds.size(); i++) {
            userTask = userTask.saveProgress(1);
            GroupUsers cur = null;
            try {
                cur = getAllGroupUsers(new UserTaskSettings(settings, false), convertedIds.get(i)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            result = utils.intersectWithCount(result, cur.getUsers());
        }
        result = ArrayUtils.sortByValue(result, 1);
        userTask.saveFinal(exportService.getStreamFromObject(result));
        return new AsyncResult<>(result);
    }

    @Override
    @Async
    public void getGroupInfoById(Integer from, Integer to, Boolean saveIds, Boolean useIds) {
        int add = 1000000;
        List<String> list;
        ExecutorService service = Executors.newFixedThreadPool(100);
        List<Future> tasks;
        for (int i = from; i < to; i += add) {

            int toCur = Math.min(to, i + add);
            int requestCount = getGroupRequestCount(toCur);
            tasks = new ArrayList<>(1000000 / requestCount);
            if (useIds) {
                List<GroupIds> idsList = groupIdRepository.findByIdBetween(i, toCur);
                list = ArrayUtils.getDelimetedLists(i, toCur, requestCount, idsList);
            } else {
                list = ArrayUtils.getDelimetedLists(from, toCur, requestCount);
            }
            for (String s : list) {
                tasks.add(service.submit(() -> {
                    List<Group> groups = getGroupInfoById(s);
                    if (saveIds) {
                        List<GroupIds> groupIdsList = groups.parallelStream().map(p -> new GroupIds(p.getId().intValue())).collect(Collectors.toList());
                        groupIdRepository.save(groupIdsList);
                    }
                    saveAll(groups);
                }));
            }
            for (Future task : tasks) {
                try {
                    task.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            service.shutdown();
        }

    }

    @Override
    public List<Integer> getUserGroups(UserTaskSettings settings, String userId) {
        UserTask userTask = new UserTask(USER_GROUPS, settings, repository);
        try {
            String url = PREFIX + "get?user_id=" + userId + ACCESS_TOKEN + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommonIDResponse response = new ResponseParser<>(CommonIDResponse.class).parseResponseString(ans, RESPONSE_STRING);
            userTask.saveFinal(exportService.getStreamFromObject(response.getItems()));
            return response.getItems();
        } catch (IOException ex) {
            userTask.saveFinalError(ex);
            ex.printStackTrace();
        } catch (JSONException e) {
            userTask.saveFinalError(e);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Future<Map<Integer, Integer>> similarGroups(UserTaskSettings settings, List<String> groups, Integer minCount) {
        return null;
    }
}
