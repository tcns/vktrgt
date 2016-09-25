package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.exception.VKException;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.external.vk.response.AudioResponse;
import ru.tcns.vktrgt.domain.external.vk.response.CommonIDResponse;
import ru.tcns.vktrgt.domain.external.vk.response.FriendsResponse;
import ru.tcns.vktrgt.domain.external.vk.response.SubscriptionsResponse;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

/**
 * Created by TIMUR on 23.04.2016.
 */
public interface VKUserService extends VKService {

    String FRIENDS_METHOD_PREFIX = "friends.";
    String USER_METHOD_PREFIX = "users.";
    String AUDIO_METHOD_PREFIX = "audio.";
    String FRIENDS_PREFIX = URL_PREFIX + FRIENDS_METHOD_PREFIX;
    String USERS_PREFIX = URL_PREFIX + USER_METHOD_PREFIX;
    String AUDIO_PREFIX = URL_PREFIX + AUDIO_METHOD_PREFIX;

    CommonIDResponse getUserFriendIds(Integer userId);
    SubscriptionsResponse getSubscriptions(String userId);
    List<User> searchUsersVK(String q, String token);
    AudioResponse getUserAudio(String userId, String token) throws VKException;

    Future<List<User>> getUserInfo(UserTaskSettings settings, List<String> userIds);
    Future<List<String>> getUserURL(UserTaskSettings settings, List<String> userIds);
    Future<List<String>> getUserId(UserTaskSettings settings, List<String> userUrls);
    Future<Map<Integer, Integer>> intersectUsers(UserTaskSettings settings, List<String> users, Integer min);
    Future<Map<Integer, Integer>> intersectSubscriptions(UserTaskSettings settings, List<String> users, Integer min);
    Future<List<Integer>> getFollowers(UserTaskSettings settings, Integer userId);
    Future<Map<String, Integer>> searchUserAudio(UserTaskSettings settings,
                                                 List<String> users, List<String> audio, String token)  throws VKException;
    Future<Set<String>> searchNearestBirthdate (UserTaskSettings settings,
                                                List<String> userIds,
                                                Integer fromDays,
                                                Integer nearestDays,
                                                List<String> types,
                                                List<Integer> gender);

    List<User> getUserInfoSync(UserTaskSettings settings, List<String> userIds);
    List<String> getUserURLSync(UserTaskSettings settings, List<String> userIds);
    List<String> getUserIdSync(UserTaskSettings settings, List<String> userUrls);
    Map<Integer, Integer> intersectUsersSync(UserTaskSettings settings, List<String> users, Integer min);
    Map<Integer, Integer> intersectSubscriptionsSync(UserTaskSettings settings, List<String> users, Integer min);
    List<Integer> getFollowersSync(UserTaskSettings settings, Integer userId);
    Map<String, Integer> searchUserAudioSync(UserTaskSettings settings,
                                                 List<String> users, List<String> audio, String token)  throws VKException;
    Set<String> searchNearestBirthdateSync (UserTaskSettings settings,
                                                List<String> userIds,
                                                Integer fromDays,
                                                Integer nearestDays,
                                                List<String> types,
                                                List<Integer> gender);
}
