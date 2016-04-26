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
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupIds;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.internal.QGroup;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.repository.external.vk.GroupIdRepository;
import ru.tcns.vktrgt.repository.external.vk.GroupRepository;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Тимур on 13.04.2016.
 */
@Service
public class GroupServiceImpl implements GroupService {


    private final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);
    @Inject
    GroupRepository groupRepository;
    @Inject
    GroupIdRepository groupIdRepository;

    @Override
    public Group findOne(Long id) {
        return groupRepository.findOne(id);
    }

    @Override
    public List<Group> saveAll(List<Group> group) {
        return groupRepository.save(group);
    }

    @Override
    public Group save(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Page<Group> findAll(Pageable pageable) {
        return groupRepository.findAll(pageable);
    }

    @Override
    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    @Override
    public void deleteAll() {
        groupRepository.deleteAll();
    }

    @Override
    public void delete(Long id) {
        groupRepository.delete(id);
    }

    @Override
    public Page<Group> searchByName(String name, Boolean restrict, Pageable pageable) {
        if (!restrict) {
            Predicate predicate = QGroup.group.name.containsIgnoreCase(name.toLowerCase());
            return groupRepository.findAll(predicate, pageable);
        } else {
            return groupRepository.findByNameIgnoreCase(name.toLowerCase(), pageable);
        }

    }

    @Override
    public List<Group> searchByNames(List<String> names) {
        ArrayList<Long> ids = new ArrayList<>();
        for (String name : names) {
            try {
                Long val = Long.parseLong(name);
                ids.add(val);
            } catch (Exception ex) {
            }

        }
        Predicate predicate = QGroup.group.screenName.in(names).or(
            QGroup.group.id.in(ids)
        );
        return Lists.newArrayList(groupRepository.findAll(predicate));
    }

    @Override
    public GroupUserResponse getGroupUsers(String groupId, int offset, int count) {
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

    @Override
    public GroupUsers getAllGroupUsers(String groupId) {
        GroupUserResponse initial = getGroupUsers(groupId, 0, 1);
        int count = initial.getCount();
        GroupUsers users = new GroupUsers(count, groupId);
        for (int i = 0; i < count; i += 1000) {
            users.append(getGroupUsers(groupId, i, 1000));
        }
        return users;
    }

    @Override
    public List<Long> intersectGroups(List<String> groups) {
        GroupUsers init = getAllGroupUsers(groups.get(0));
        ArrayUtils utils = new ArrayUtils();
        List<Long> result = init.getUsers();
        for (int i = 1; i < groups.size(); i++) {
            GroupUsers cur = getAllGroupUsers(groups.get(i));
            result = utils.intersect(result, cur.getUsers());
        }
        return result;
    }

    @Override
    public List<Group> getGroupInfoById(String ids) {
        String fields = "city,country,place,description,wiki_page,members_count,counters,start_date,finish_date," +
            "public_date_label,activity,status,contacts,links,fixed_post,verified,site,main_album_id,main_section,market";

        List<Group> groups = new ArrayList<>();
        Content content = null;
        try {
            String url = PREFIX + "getById?group_ids=" + ids + "&fields=" + fields;
            content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            List<Group> groupResponse = VKResponseParser.parseGroupGetByIdResponse(ans);
            if (groupResponse != null) {
                groups.addAll(groupResponse);
            } else {
                System.out.println(ans);
                System.out.println(ids);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return groups;
    }

    @Override
    @Async
    public void getGroupInfoById(Integer from, Integer to, Boolean saveIds, Boolean useIds) {
        int add = 100000;
        List<String> list;
        for (int i = from; i < to; i += add) {
            int toCur = Math.min(to, i+add);
            int requestCount = getGroupRequestCount(toCur);
            if (useIds) {
                List<GroupIds> idsList = groupIdRepository.findByIdBetween(i, toCur);
                list = ArrayUtils.getDelimetedLists(i, toCur, requestCount, idsList);
            } else {
                list = ArrayUtils.getDelimetedLists(from, toCur, requestCount);
            }
            for (String s : list) {
                List<Group> groups = getGroupInfoById(s);
                if (saveIds) {
                    List<GroupIds> groupIdsList = groups.stream().map(p -> new GroupIds(p.getId().intValue())).collect(Collectors.toList());
                    groupIdRepository.save(groupIdsList);
                }
                saveAll(groups);
            }
        }

    }

    @Override
    public List<Long> getUserGroups(String userId) {
        try {
            String url = PREFIX + "get?user_id=" + userId + ACCESS_TOKEN;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            CommonIDResponse response = VKResponseParser.parseCommonIDResponse(ans);
            return response.getIds();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private int getGroupRequestCount(int toCur) {
        int requestCount = VKDicts.MAX_GROUP_REQUEST_COUNT;
        if (toCur >= 1000000) {
            requestCount = 400;
        }
        if (toCur >= 10000000) {
            requestCount = 350;
        }
        if (toCur >= 100000000) {
            requestCount = 300;
        }
        return requestCount;
    }

}
