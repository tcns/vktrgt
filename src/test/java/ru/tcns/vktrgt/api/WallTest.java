package ru.tcns.vktrgt.api;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.external.vk.impl.ActivityServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.GroupServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.VKUserServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.WallServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.StrictAssertions.assertThat;

/**
 * Created by TIMUR on 02.05.2016.
 */
public class WallTest {
    @Test
    public void testUsersInfo() {
        /*Long cur = System.currentTimeMillis();
        ActivityServiceImpl activityService = new ActivityServiceImpl();
        activityService.setWallService(new WallServiceImpl());
        Map<Integer, Map<Integer, Integer>> response = activityService.getActiveTopicAuditory(
            Arrays.asList("https://vk.com/topic-4100014_22054206"), 0
        );
        Long time = (System.currentTimeMillis() - cur) / 1000L;
        Assertions.assertThat(response).isNotNull();*/
    }
}
