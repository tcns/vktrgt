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
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.QGroup;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.util.parser.ResponseParser;
import ru.tcns.vktrgt.domain.util.parser.VKResponseParser;
import ru.tcns.vktrgt.repository.external.vk.GroupIdRepository;
import ru.tcns.vktrgt.repository.external.vk.GroupRepository;
import ru.tcns.vktrgt.service.external.vk.intf.GroupService;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TIMUR on 03.05.2016.
 */
public abstract class AbstractGroupService implements GroupService {

    protected final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);
    @Inject
    protected GroupRepository groupRepository;
    @Inject
    protected GroupIdRepository groupIdRepository;
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
    protected List<Group> getGroupInfoById(String ids) {
        String fields = "city,country,place,description,wiki_page,members_count,start_date,finish_date," +
            "public_date_label,activity,status,contacts,links,fixed_post,verified,site,main_album_id,main_section,market";

        List<Group> groups = new ArrayList<>();
        Content content = null;
        try {
            String url = PREFIX + "getById?group_ids=" + ids + "&fields=" + fields+VERSION;
            content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            List<Group> groupResponse = VKResponseParser.parseGroupGetByIdResponse(ans);
            if (groupResponse != null) {
                groups.addAll(groupResponse);
            } else {
                System.out.println(ans);
                System.out.println(ids);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return groups;
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
    protected int getGroupRequestCount(int toCur) {
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
    protected CommonIDResponse getGroupUsers(String groupId, int offset, int count) {
        CommonIDResponse response = new CommonIDResponse();
        try {
            String url = PREFIX + "getMembers?group_id=" + groupId + "&offset=" + offset
                + "&count=" + count + VERSION;
            Content content = Request.Get(url).execute().returnContent();
            String ans = content.asString();
            response = new ResponseParser<>(CommonIDResponse.class).parseResponseString(ans, RESPONSE_STRING);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
}
