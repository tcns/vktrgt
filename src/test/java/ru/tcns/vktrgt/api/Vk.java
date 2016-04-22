package ru.tcns.vktrgt.api;

import org.junit.Test;
import ru.tcns.vktrgt.api.vk.Common;
import ru.tcns.vktrgt.api.vk.Groups;
import ru.tcns.vktrgt.api.vk.Users;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;

import java.util.Arrays;
import java.util.HashMap;
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
        Map<Long, Integer> response = Users.intersectUsers(Arrays.asList(14838024L, 10541396L));
        Long time = (System.currentTimeMillis() - cur)/1000L;
        assertThat(response).isNotNull();
    }

}
