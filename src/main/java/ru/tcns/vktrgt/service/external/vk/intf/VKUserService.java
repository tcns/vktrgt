package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.UserTask;
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

    public static final String USER_INFO = "userInfo";
    public static final String FOLLOWERS = "followers";
    public static final String SUBSCRIPTIONS = "user-intersect";
    public static final String LEADERS = "user-leaders";
    public static final String USER_URL = "user-ids";
    public static final String USER_IDS = "user-urls";
    public static final String AUDIO = "user-audio";
    public static final String NEAREST_DATES = "user-birth";

    CommonIDResponse getUserFriendIds(Integer userId);
    SubscriptionsResponse getSubscriptions(String userId);
    List<User> searchUsersVK(String q, String token);
    AudioResponse getUserAudio(String userId, String token) throws VKException;

    Future<List<User>> getUserInfo(UserTask task, List<String> userIds);
    Future<List<String>> getUserURL(UserTask task, List<String> userIds);
    Future<List<String>> getUserId(UserTask task, List<String> userUrls);
    Future<Map<Integer, Integer>> intersectUsers(UserTask task, List<String> users, Integer min);
    Future<Map<Integer, Integer>> intersectSubscriptions(UserTask task, List<String> users, Integer min);
    Future<List<Integer>> getFollowers(UserTask task, Integer userId);
    Future<Map<String, Integer>> searchUserAudio(UserTask task,
                                                 List<String> users, List<String> audio, String token)  throws VKException;
    Future<Set<String>> searchNearestBirthdate (UserTask task,
                                                List<String> userIds,
                                                Integer fromDays,
                                                Integer nearestDays,
                                                List<String> types,
                                                List<Integer> gender);

    List<User> getUserInfoSync(UserTask task, List<String> userIds);
    List<String> getUserURLSync(UserTask task, List<String> userIds);
    List<String> getUserIdSync(UserTask task, List<String> userUrls);
    Map<Integer, Integer> intersectUsersSync(UserTask task, List<String> users, Integer min);
    Map<Integer, Integer> intersectSubscriptionsSync(UserTask task, List<String> users, Integer min);
    List<Integer> getFollowersSync(UserTask task, Integer userId);
    Map<String, Integer> searchUserAudioSync(UserTask task,
                                                 List<String> users, List<String> audio, String token)  throws VKException;
    Set<String> searchNearestBirthdateSync (UserTask task,
                                                List<String> userIds,
                                                Integer fromDays,
                                                Integer nearestDays,
                                                List<String> types,
                                                List<Integer> gender);
}
