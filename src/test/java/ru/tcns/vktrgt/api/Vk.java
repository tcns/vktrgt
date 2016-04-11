package ru.tcns.vktrgt.api;

import org.junit.Test;
import ru.tcns.vktrgt.api.vk.Common;
import ru.tcns.vktrgt.api.vk.Groups;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;
import ru.tcns.vktrgt.domain.external.vk.response.GroupResponse;
import ru.tcns.vktrgt.domain.util.ArrayUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/**
 * Created by timur on 3/26/16.
 */
public class Vk {

    @Test
    public void testGroupResponse() {
        Long cur = System.currentTimeMillis();
        List<Group> response = Groups.getGroupInfoById(ArrayUtils.getDelimetedLists(1, 50000, 500));
        Long time = (System.currentTimeMillis() - cur)/1000L;
        assertThat(response).isNotNull();
    }

}
