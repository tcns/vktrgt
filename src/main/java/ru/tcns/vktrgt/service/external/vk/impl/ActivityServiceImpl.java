package ru.tcns.vktrgt.service.external.vk.impl;

import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.service.external.vk.intf.ActivityService;
import ru.tcns.vktrgt.service.external.vk.intf.WallService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 30.04.2016.
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Inject
    private WallService wallService;

    @Override
    public Map<Integer, Map<Integer, Integer>> getActiveAuditory(ActiveAuditoryDTO activeAuditoryDTO) {
        Map<Integer, Map<Integer, Integer>> activity = new HashMap<>();
        Map<Integer, List<WallPost>> wallPosts = new HashMap<>();
        for (Integer i : activeAuditoryDTO.getGroups()) {
            wallPosts.put(i, getWallService().getWallPosts(i, activeAuditoryDTO.getMaxDays()));
        }
        if (activeAuditoryDTO.getPostIds() != null &&
            !activeAuditoryDTO.getPostIds().isEmpty()) {
            List<WallPost> posts = new ArrayList<>();
            for (Integer id: activeAuditoryDTO.getPostIds()) {
                WallPost post = new WallPost();
                post.setId(id);
                posts.add(post);
            }
            wallPosts.put(0, posts);
        }
        ArrayUtils utils = new ArrayUtils();
        if (activeAuditoryDTO.getCountByAllGroups()) {
            activity.put(0, new HashMap<>());
        }
        for (Map.Entry<Integer, List<WallPost>> e : wallPosts.entrySet()) {
            Map<Integer, Integer> groupActivity = new HashMap<>();
            if (activeAuditoryDTO.getCountByAllGroups()) {
                groupActivity = activity.get(0);
            }
            for (WallPost post : e.getValue()) {
                if (activeAuditoryDTO.getCountComments()) {
                    List<Integer> list = getWallService().getComments(e.getKey(), post.getId());
                    groupActivity = utils.intersectWithCount(groupActivity, list);
                }
                if (activeAuditoryDTO.getCountLikes()) {
                    List<Integer> list = getWallService().getLikes(e.getKey(), post.getId(), activeAuditoryDTO.getType());
                    groupActivity = utils.intersectWithCount(groupActivity, list);
                }
                if (activeAuditoryDTO.getCountReposts()) {
                    List<Integer> list = getWallService().getReposts(e.getKey(), post.getId());
                    groupActivity = utils.intersectWithCount(groupActivity, list);
                }
            }
            if (activeAuditoryDTO.getCountByAllGroups()) {
                activity.put(0, groupActivity);
            } else {
                groupActivity = ArrayUtils.sortByValue(groupActivity, activeAuditoryDTO.getMinCount());
                activity.put(e.getKey(), groupActivity);
            }
        }
        if (activeAuditoryDTO.getCountByAllGroups()) {
            activity.put(0, ArrayUtils.sortByValue(activity.get(0), activeAuditoryDTO.getMinCount()));
        }
        return activity;
    }

    public WallService getWallService() {
        return wallService;
    }

    public void setWallService(WallService wallService) {
        this.wallService = wallService;
    }
}
