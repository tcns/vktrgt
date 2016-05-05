package ru.tcns.vktrgt.api;

import org.junit.Test;
import ru.tcns.vktrgt.api.vk.Common;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.service.external.vk.impl.ActivityServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.GroupServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.WallServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by TIMUR on 02.05.2016.
 */
public class GroupsTest {
    //@Test
    public void testUserGroups() {
        /*Long cur = System.currentTimeMillis();
        GroupServiceImpl groupService = new GroupServiceImpl();
        List<Integer> response = groupService.getUserGroups("1330216");
        Long time = (System.currentTimeMillis() - cur) / 1000L;
        assertThat(response).isNotNull();*/
    }
    //@Test
    public void testGroupUsers() {
        /*Long cur = System.currentTimeMillis();
        GroupServiceImpl groupService = new GroupServiceImpl();
        GroupUsers response = groupService.getAllGroupUsers("mudachyo");
        Long time = (System.currentTimeMillis() - cur) / 1000L;
        assertThat(response).isNotNull();*/
    }
    @Test
    public void testVKSearch() {
        GroupServiceImpl groupService = new GroupServiceImpl();
        List<Group> groups = groupService.searchVk("mdk", "af84afcbd82ddbbd8321467b8dd8cf0bd768f04a6279bb8d34085b3b99e9238827d115f7f79712fd098ab");
        assertThat(groups).isNotNull();

    }

}
