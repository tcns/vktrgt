package ru.tcns.vktrgt.service.external.vk.intf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.tcns.vktrgt.api.vk.Common;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;

import javax.lang.model.element.Name;
import java.util.List;

/**
 * Created by Тимур on 13.04.2016.
 */
public interface GroupService extends VKService {
    String METHOD_PREFIX = "groups.";
    String PREFIX = URL_PREFIX + METHOD_PREFIX;

    Group findOne(Long id);
    Group save(Group group);
    List<Group> saveAll(List<Group> group);
    Page<Group> findAll(Pageable pageable);
    List<Group> findAll();
    void delete(Long id);
    void deleteAll();
    Page<Group> searchByName(String name, Boolean restrict, Pageable pageable);
    List<Group> searchByNames(List<String> names);
    GroupUserResponse getGroupUsers(String groupId, int offset, int count);
    GroupUsers getAllGroupUsers(String groupId);
    List<Integer> intersectGroups(List<String> groups);
    List<Group> getGroupInfoById(String ids);
    void getGroupInfoById(Integer from, Integer to, Boolean saveIds, Boolean useIds);
    List<Integer> getUserGroups(String userId);

}
