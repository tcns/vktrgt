package ru.tcns.vktrgt.service.external.vk.impl;

import com.google.common.collect.Lists;
import com.mysema.query.types.Predicate;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupIds;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.internal.QGroup;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.repository.external.vk.GroupIdRepository;
import ru.tcns.vktrgt.repository.external.vk.GroupRepository;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    @Async
    public Future<GroupUsers> getAllGroupUsers(UserTaskSettings settings, String groupId) {
        UserTask userTask = new UserTask(ALL_USERS, settings, repository);
        CommonIDResponse initial = getGroupUsers(groupId, 0, 1);
        int count = initial.getCount();
        userTask = userTask.saveInitial(count);
        ExecutorService service = Executors.newFixedThreadPool(10);
        GroupUsers users = new GroupUsers(count, groupId);
        List<Future<CommonIDResponse>> tasks = new ArrayList<>();
        for (int i = 0; i < count; i += 1000) {
            final int cur = i;
            try {
                tasks.add(service.submit(() -> getGroupUsers(groupId, cur, 1000)));
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
        userTask.saveFinal(users);
        return new AsyncResult<>(users);
    }

    @Override
    @Async
    public Future<List<Integer>> intersectGroups(UserTaskSettings settings, List<String> groups) {
        UserTask userTask = new UserTask(INTERSECT_GROUPS, settings, repository);
        GroupUsers init = null;
        try {
            init = getAllGroupUsers(settings, groups.get(0)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        ArrayUtils utils = new ArrayUtils();
        List<Integer> result = init.getUsers();
        userTask = userTask.saveInitial(groups.size());
        for (int i = 1; i < groups.size(); i++) {
            userTask = userTask.saveProgress(1);
            GroupUsers cur = null;
            try {
                cur = getAllGroupUsers(new UserTaskSettings(settings.getUser(), false, settings.getTaskDescription()), groups.get(i)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            result = utils.intersect(result, cur.getUsers());
        }
        userTask.saveFinal(result);
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
                        List<GroupIds> groupIdsList = groups.stream().map(p -> new GroupIds(p.getId().intValue())).collect(Collectors.toList());
                        groupIdRepository.save(groupIdsList);
                    }
                    saveAll(groups);
                }));
            }
            for (Future task: tasks) {
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
            userTask.saveFinal(response.getItems());
            return response.getItems();
        } catch (IOException ex) {
            userTask.saveError(ex);
            ex.printStackTrace();
        } catch (JSONException e) {
            userTask.saveError(e);
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

}
