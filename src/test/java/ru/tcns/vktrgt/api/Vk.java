package ru.tcns.vktrgt.api;

import org.junit.Test;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by timur on 3/26/16.
 */
public class Vk {

    @Test
    public void testGroupResponse() {
        Long cur = System.currentTimeMillis();
        Map<Long, Integer> response = VKUserService.intersectUsers(Arrays.asList(14838024L, 10541396L));
        Long time = (System.currentTimeMillis() - cur)/1000L;
        assertThat(response).isNotNull();
    }

}
