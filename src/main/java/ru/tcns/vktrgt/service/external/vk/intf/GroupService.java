package ru.tcns.vktrgt.service.external.vk.intf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.exception.VKException;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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
    List<Group> searchVk(String q, String token) throws VKException;

    List<Integer> getUserGroups(UserTaskSettings settings, String userId);

    Future<GroupUsers> getAllGroupUsers(UserTaskSettings settings, String groupId);
    Future<Map<Integer, Integer>> intersectGroups(UserTaskSettings settings, List<String> groups, Integer minCount);
    Future<List<Group>> getGroupsInfo(UserTaskSettings settings, List<String> groups);
    void getGroupInfoById(Integer from, Integer to, Boolean saveIds, Boolean useIds);
    Future<Map<Integer, Integer>> similarGroups (UserTaskSettings settings, List<String> groups, Integer minCount);

    GroupUsers getAllGroupUsersSync(UserTaskSettings settings, String groupId);
    Map<Integer, Integer> intersectGroupsSync(UserTaskSettings settings, List<String> groups, Integer minCount);
    List<Group> getGroupsInfoSync(UserTaskSettings settings, List<String> groups);
    void getGroupInfoByIdSync(Integer from, Integer to, Boolean saveIds, Boolean useIds);
    Map<Integer, Integer> similarGroupsSync (UserTaskSettings settings, List<String> groups, Integer minCount);

}
