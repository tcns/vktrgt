package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;

import java.util.List;

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

    List<WallPost> getWallPosts(Integer ownerId, Integer maxDays);

    List<Integer> getLikes (Integer ownerId, Integer postId, String type);
    List<Integer> getComments (Integer ownerId, Integer postId);
    List<Integer> getTopicCommentsWithLikes (Integer ownerId, Integer postId);
    List<Integer> getReposts (Integer ownerId, Integer postId);

}
