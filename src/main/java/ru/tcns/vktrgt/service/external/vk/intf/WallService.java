package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.User;
import ru.tcns.vktrgt.domain.UserTask;
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
    public static String BEAN_NAME = "WallServiceImpl";
    public static String TOPIC_COMMENTS = BEAN_NAME + "TopicComments";

    Future<List<WallPost>> getWallPosts(UserTask task, Integer ownerId, Integer maxDays);
    Future<List<Integer>> getLikes (UserTask task, Integer ownerId, Integer postId, String type);
    Future<List<Integer>> getComments (UserTask task, Integer ownerId, Integer postId);
    Future<List<Integer>> getTopicCommentsWithLikes (UserTask task, Integer ownerId, Integer postId);
    Future<List<Integer>> getReposts (UserTask task, Integer ownerId, Integer postId);

    List<WallPost> getWallPostsSync(UserTask task, Integer ownerId, Integer maxDays);
    List<Integer> getLikesSync (UserTask task, Integer ownerId, Integer postId, String type);
    List<Integer> getCommentsSync (UserTask task, Integer ownerId, Integer postId);
    List<Integer> getTopicCommentsWithLikesSync (UserTask task, Integer ownerId, Integer postId);
    List<Integer> getRepostsSync (UserTask task, Integer ownerId, Integer postId);

}
