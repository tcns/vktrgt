package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.json.JSONException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.exception.VKException;
import ru.tcns.vktrgt.domain.external.vk.internal.*;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.external.vk.response.VKErrorResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKUrlParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Created by Тимур on 13.04.2016.
 */
@Service
public class GroupServiceImpl extends AbstractGroupService {


    @Inject
    UserTaskRepository repository;
    @Inject
    ExportService exportService;
    @Inject
    VKUserService userService;

    @Override
    public List<Group> searchVk(String q, String token) throws VKException{
        List<Group> groups = new ArrayList<>();
        Content content = null;
        try {
            String url = PREFIX + "search?q=" + q + "&count=1000&access_token=" + token + VERSION;
            content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            GroupResponse groupResponse = new ResponseParser<>(GroupResponse.class).parseResponseString(ans, RESPONSE_STRING);
            if (groupResponse != null) {
                groups.addAll(groupResponse.getItems());
            } else {
                VKErrorResponse vkErrorResponse = new ResponseParser<>(VKErrorResponse.class).parseResponseString(ans, ERROR_STRING);
                throw new VKException(vkErrorResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    @Async
    public Future<GroupUsers> getAllGroupUsers(UserTask userTask, String groupId) {
        return new AsyncResult<>(getAllGroupUsersSync(userTask, groupId));
    }

    @Override
    @Async
    public Future<List<Group>> getGroupsInfo(UserTask userTask, List<String> groups) {
        return new AsyncResult<>(getGroupsInfoSync(userTask, groups));
    }

    @Override
    @Async
    public Future<Map<Integer, Integer>> intersectGroups(UserTask userTask, List<String> groups, Integer minCount) {
        return new AsyncResult<>(intersectGroupsSync(userTask, groups, minCount));
    }

    @Override
    @Async
    public void getGroupInfoById(Integer from, Integer to, Boolean saveIds, Boolean useIds) {
        getGroupInfoByIdSync(from, to, saveIds, useIds);

    }

    @Override
    public List<Integer> getUserGroups(UserTask userTask, String userId) {
        userTask = userTask.startWork();
        try {
            String url = PREFIX + "get?user_id=" + userId + ACCESS_TOKEN + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommonIDResponse response = new ResponseParser<>(CommonIDResponse.class).parseResponseString(ans, RESPONSE_STRING);
            userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(response.getItems(), "\n")));
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
    public Future<Map<Integer, Integer>> similarGroups(UserTask userTask, List<String> groups, Integer minCount) {
        return null;
    }

    @Override
    public GroupUsers getAllGroupUsersSync(UserTask userTask, String groupId) {
        userTask = userTask.startWork();
        CommonIDResponse initial = getGroupUsers(groupId, 0, 1);
        if (initial == null || initial.getCount() == 0) {
            return new GroupUsers(0, groupId);
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
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(users.getUsers(), "\n")));
        return users;
    }

    @Override
    public Map<Integer, Integer> intersectGroupsSync(UserTask userTask, List<String> groups, Integer minCount) {
        userTask = userTask.startWork();
        ArrayUtils utils = new ArrayUtils();
        List<String> convertedIds = groups.parallelStream().map(a->VKUrlParser.getName(a)).collect(Collectors.toList());
        userTask = userTask.saveInitial(convertedIds.size());
        Map<Integer, Integer> result = new HashMap<>();
        for (int i = 0; i < convertedIds.size(); i++) {
            userTask = userTask.saveProgress(1);
            GroupUsers cur = getAllGroupUsersSync(userTask.copyNoCreate(), convertedIds.get(i));
            result = utils.intersectWithCount(result, cur.getUsers());
        }
        result = ArrayUtils.sortByValue(result, minCount);
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(result.keySet(), "\n")));
        return result;
    }

    @Override
    public List<Group> getGroupsInfoSync(UserTask userTask, List<String> groups) {
        List<Group> response = new ArrayList<>();
        userTask = userTask.startWork();
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
        return response;
    }

    @Override
    public void getGroupInfoByIdSync(Integer from, Integer to, Boolean saveIds, Boolean useIds) {
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
    public Map<Integer, Integer> similarGroupsSync(UserTask userTask, List<String> groups, Integer minCount) {
        return null;
    }
}
