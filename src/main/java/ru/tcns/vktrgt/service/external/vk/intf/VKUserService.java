package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.FriendsResponse;
import ru.tcns.vktrgt.domain.external.vk.response.SubscriptionsResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 23.04.2016.
 */
public interface VKUserService extends VKService {

    String FRIENDS_METHOD_PREFIX = "friends.";
    String USER_METHOD_PREFIX = "users.";
    String FRIENDS_PREFIX = URL_PREFIX + FRIENDS_METHOD_PREFIX;
    String USERS_PREFIX = URL_PREFIX + USER_METHOD_PREFIX;
    FriendsResponse getUserFriends(Integer userId);
    List<User> getUserRelatives(List<Integer> userId);

    CommonIDResponse getUserFriendIds(Integer userId);

    SubscriptionsResponse getSubscriptions(String userId);

    Map<Integer, Integer> intersectUsers(List<Integer> users, Integer min);
    Map<Integer, Integer> intersectSubscriptions(List<Integer> users, Integer min);

    List<Integer> getFollowers(Integer userId);
}
