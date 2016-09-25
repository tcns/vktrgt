package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.User;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by TIMUR on 29.04.2016.
 */
public interface WallService extends VKService {
    String METHOD_PREFIX = "wall.";
    String LIKES_METHOD_PREFIX = "likes.";
    String TOPIC_METHOD_PREFIX = "board.";
    String PREFIX = URL_PREFIX + METHOD_PREFIX;
    String LIKES_PREFIX = URL_PREFIX + LIKES_METHOD_PREFIX;
    String TOPIC_PREFIX = URL_PREFIX + TOPIC_METHOD_PREFIX;

    Future<List<WallPost>> getWallPosts(UserTaskSettings settings, Integer ownerId, Integer maxDays);
    Future<List<Integer>> getLikes (UserTaskSettings settings, Integer ownerId, Integer postId, String type);
    Future<List<Integer>> getComments (UserTaskSettings settings, Integer ownerId, Integer postId);
    Future<List<Integer>> getTopicCommentsWithLikes (UserTaskSettings settings, Integer ownerId, Integer postId);
    Future<List<Integer>> getReposts (UserTaskSettings settings, Integer ownerId, Integer postId);

    List<WallPost> getWallPostsSync(UserTaskSettings settings, Integer ownerId, Integer maxDays);
    List<Integer> getLikesSync (UserTaskSettings settings, Integer ownerId, Integer postId, String type);
    List<Integer> getCommentsSync (UserTaskSettings settings, Integer ownerId, Integer postId);
    List<Integer> getTopicCommentsWithLikesSync (UserTaskSettings settings, Integer ownerId, Integer postId);
    List<Integer> getRepostsSync (UserTaskSettings settings, Integer ownerId, Integer postId);

}
