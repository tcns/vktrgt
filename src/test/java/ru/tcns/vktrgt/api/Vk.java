package ru.tcns.vktrgt.api;

import org.junit.Test;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;
import ru.tcns.vktrgt.service.external.vk.impl.ActivityServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.WallServiceImpl;

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
        ActivityServiceImpl activityService = new ActivityServiceImpl();
        activityService.setWallService(new WallServiceImpl());
        Map<Integer, Map<Integer, Integer>> response = activityService.getActiveAuditory(new ActiveAuditoryDTO(Arrays.asList(-4100014), 100, false, true, true, true, 0, "post",
            null));
        Long time = (System.currentTimeMillis() - cur)/1000L;
        assertThat(response).isNotNull();
    }

}
