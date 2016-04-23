package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.FriendsResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 23.04.2016.
 */
public interface VKUserService {
    FriendsResponse getUserFriends(Long userId);

    CommonIDResponse getUserFriendIds(Long userId);

    Map<Long, Integer> intersectUsers(List<Long> users, Integer min);
}
