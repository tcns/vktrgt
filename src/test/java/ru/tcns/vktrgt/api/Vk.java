package ru.tcns.vktrgt.api;

import org.junit.Test;
import ru.tcns.vktrgt.domain.external.vk.response.SubscriptionsResponse;
import ru.tcns.vktrgt.service.external.vk.impl.GroupServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.VKUserServiceImpl;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by timur on 3/26/16.
 */
public class Vk {

    @Test
    public void testGroupResponse() {
        Long cur = System.currentTimeMillis();
        VKUserServiceImpl  vkUserService = new VKUserServiceImpl();
        GroupServiceImpl groupService = new GroupServiceImpl();
        ArrayList<Long> ids = groupService.getAllGroupUsers("52673292").getUsers();
        Map<Long, Integer> response = vkUserService.intersectSubscriptions(ids, 200);
        Long time = (System.currentTimeMillis() - cur)/1000L;
        assertThat(response).isNotNull();
    }

}
