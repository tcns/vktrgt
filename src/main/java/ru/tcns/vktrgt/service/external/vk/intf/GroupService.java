package ru.tcns.vktrgt.service.external.vk.intf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.tcns.vktrgt.domain.UserTask;
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
    public static final String GROUP_MEMBERS = "group-members";
    public static final String INTERSECT_GROUPS = "group-intersect";
    public static final String GROUP_INFO = "group-info";
    public static final String SEARCH_GROUP = "GroupSearch";
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

    List<Integer> getUserGroups(UserTask task, String userId);

    Future<GroupUsers> getAllGroupUsers(UserTask task, String groupId);
    Future<Map<Integer, Integer>> intersectGroups(UserTask task, List<String> groups, Integer minCount);
    Future<List<Group>> getGroupsInfo(UserTask task, List<String> groups);
    void getGroupInfoById(Integer from, Integer to, Boolean saveIds, Boolean useIds);
    Future<Map<Integer, Integer>> similarGroups (UserTask task, List<String> groups, Integer minCount);

    GroupUsers getAllGroupUsersSync(UserTask task, String groupId);
    Map<Integer, Integer> intersectGroupsSync(UserTask task, List<String> groups, Integer minCount);
    List<Group> getGroupsInfoSync(UserTask task, List<String> groups);
    void getGroupInfoByIdSync(Integer from, Integer to, Boolean saveIds, Boolean useIds);
    Map<Integer, Integer> similarGroupsSync (UserTask task, List<String> groups, Integer minCount);

}
