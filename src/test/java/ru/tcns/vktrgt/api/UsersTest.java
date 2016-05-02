package ru.tcns.vktrgt.api;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import org.junit.Test;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.external.vk.impl.GroupServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.VKUserServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.StrictAssertions.assertThat;

/**
 * Created by TIMUR on 02.05.2016.
 */
public class UsersTest {
    @Test
    public void testUsersInfo() {
        Long cur = System.currentTimeMillis();
        GroupServiceImpl groupService = new GroupServiceImpl();
        GroupUsers response = groupService.getAllGroupUsers("newalbums");
        VKUserServiceImpl vkUserService = new VKUserServiceImpl();
        List<User> users = vkUserService.getUserInfo(Lists.transform(response.getUsers(), Functions.toStringFunction()));
        Long time = (System.currentTimeMillis() - cur) / 1000L;
        assertThat(users).isNotNull();
    }
}
