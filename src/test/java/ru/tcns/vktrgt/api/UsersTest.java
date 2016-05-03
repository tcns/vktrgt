package ru.tcns.vktrgt.api;

import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang.math.Range;
import org.junit.Test;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.GroupUsers;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.service.external.vk.impl.AnalysisServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.GroupServiceImpl;
import ru.tcns.vktrgt.service.external.vk.impl.VKUserServiceImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.StrictAssertions.assertThat;

/**
 * Created by TIMUR on 02.05.2016.
 */
public class UsersTest {
    @Test
    public void testUsersInfo() {
        /*Long cur = System.currentTimeMillis();
        GroupServiceImpl groupService = new GroupServiceImpl();
        GroupUsers response = groupService.getAllGroupUsers("newalbums");
        VKUserServiceImpl vkUserService = new VKUserServiceImpl();
        List<User> users = vkUserService.getUserInfo(Lists.transform(response.getUsers(), Functions.toStringFunction()));
        AnalysisServiceImpl analysisService = new AnalysisServiceImpl();
        List<User> analyseDTO = analysisService.filterUsers(users, getDto());
        Long time = (System.currentTimeMillis() - cur) / 1000L;
        assertThat(analyseDTO).isNotNull();*/
    }
    private AnalyseDTO getDto() {
        AnalyseDTO dto = new AnalyseDTO();
        Map<Integer, List<User>> sex = new HashMap<>();
        sex.put(1, null);
        Map<IntRange, List<User>> range = new HashMap<>();
        range.put(new IntRange(22, 24), null);
        dto.setSex(sex);
        dto.setAge(range);
        return dto;
    }
}
