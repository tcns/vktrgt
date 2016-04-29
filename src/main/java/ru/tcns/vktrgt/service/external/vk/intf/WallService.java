package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;

import java.util.List;

/**
 * Created by TIMUR on 29.04.2016.
 */
public interface WallService extends VKService {
    String METHOD_PREFIX = "wall.";
    String PREFIX = URL_PREFIX + METHOD_PREFIX;

    List<WallPost> getWallPosts(Integer wallId, Integer maxDays);

}
