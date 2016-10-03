package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.VKUrlDto;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.VKUrlParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.vk.intf.ActivityService;
import ru.tcns.vktrgt.service.external.vk.intf.WallService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by TIMUR on 30.04.2016.
 */
@Service
public class ActivityServiceImpl implements ActivityService {



    @Inject
    private WallService wallService;
    @Inject
    private ExportService exportService;

    @Override
    @Async
    public Future<Map<Integer, Integer>> getActiveTopicAuditory(UserTask settings, List<String> topicUrls, Integer minCount) {
        return new AsyncResult<>(getActiveTopicAuditorySync(settings, topicUrls, minCount));
    }

    @Override
    public Map<Integer, Map<Integer, Integer>> getActiveAuditorySync(UserTask userTask, ActiveAuditoryDTO activeAuditoryDTO) {
        Map<Integer, Map<Integer, Integer>> activity = new HashMap<>();
        userTask = userTask.startWork();
        Map<Integer, List<WallPost>> wallPosts = new HashMap<>();
        userTask = userTask.saveInitial(activeAuditoryDTO.getGroups().size());
        userTask = userTask.updateStatusMessage("Сбор постов со стены");
        for (String i : activeAuditoryDTO.getGroups()) {
            try {
                Integer val = Integer.valueOf(i);
                wallPosts.put(val, getWallService().getWallPostsSync(userTask.copyNoCreate(),
                    val, activeAuditoryDTO.getMaxDays()));
            } catch (NumberFormatException e){e.printStackTrace();}
        }
        userTask = userTask.updateStatusMessage("Обработка постов");
        if (activeAuditoryDTO.getPostIds() != null &&
            !activeAuditoryDTO.getPostIds().isEmpty()) {
            List<WallPost> posts = new ArrayList<>();
            for (String id : activeAuditoryDTO.getPostIds()) {
                try {
                    WallPost post = new WallPost();
                    post.setId(Integer.valueOf(id));
                    posts.add(post);
                } catch (NumberFormatException e){}

            }
            wallPosts.put(0, posts);
        }
        ArrayUtils utils = new ArrayUtils();
        if (activeAuditoryDTO.getCountByAllGroups()) {
            activity.put(0, new HashMap<>());
        }
        userTask = userTask.saveInitial(countSteps(activeAuditoryDTO, wallPosts));
        for (Map.Entry<Integer, List<WallPost>> e : wallPosts.entrySet()) {
            Map<Integer, Integer> groupActivity = new HashMap<>();
            if (activeAuditoryDTO.getCountByAllGroups()) {
                groupActivity = activity.get(0);
            }
            for (WallPost post : e.getValue()) {
                if (activeAuditoryDTO.getCountComments()) {

                    userTask = userTask.updateStatusMessage("Сбор комментариев");
                    List<Integer> list = getWallService().getCommentsSync(userTask.copyNoCreate(), e.getKey(), post.getId());
                    groupActivity = utils.intersectWithCount(groupActivity, list);
                    userTask = userTask.saveProgress(1);
                }
                if (activeAuditoryDTO.getCountLikes()) {

                    userTask = userTask.updateStatusMessage("Сбор мне нравится");
                    List<Integer> list = getWallService().getLikesSync(userTask.copyNoCreate(), e.getKey(), post.getId(), activeAuditoryDTO.getType());
                    groupActivity = utils.intersectWithCount(groupActivity, list);
                    userTask = userTask.saveProgress(1);
                }
                if (activeAuditoryDTO.getCountReposts()) {
                    userTask = userTask.updateStatusMessage("Сбор репостов");

                    List<Integer> list = getWallService().getRepostsSync(userTask.copyNoCreate(), e.getKey(), post.getId());
                    groupActivity = utils.intersectWithCount(groupActivity, list);
                    userTask = userTask.saveProgress(1);
                }
            }
            userTask = userTask.updateStatusMessage("Обработка результатов");

            if (activeAuditoryDTO.getCountByAllGroups()) {
                activity.put(0, groupActivity);
            } else {
                groupActivity = ArrayUtils.sortByValue(groupActivity, activeAuditoryDTO.getMinCount());
                activity.put(e.getKey(), groupActivity);
            }
            userTask = userTask.saveProgress(1);
        }
        if (activeAuditoryDTO.getCountByAllGroups()) {
            activity.put(0, ArrayUtils.sortByValue(activity.get(0), activeAuditoryDTO.getMinCount()));
            userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(activity.get(0).keySet(), "\n")));
        } else {
            StringBuilder sb = new StringBuilder();
            for (Integer key: activity.keySet()) {
                sb.append(key + ": \n");
                for (Integer userId: activity.get(key).keySet()) {
                    sb.append("\t" + userId + "\n");
                }
            }
            userTask.saveFinal(exportService.getStreamFromObject(sb.toString()));
        }

        return activity;
    }

    @Override
    public Map<Integer, Integer> getActiveTopicAuditorySync(UserTask userTask, List<String> topicUrls, Integer minCount) {
        userTask = userTask.startWork();
        userTask = userTask.saveInitial(topicUrls.size() * 2);
        Map<Integer, List<Integer>> wallPosts = new HashMap<>();
        for (String url : topicUrls) {
            userTask = userTask.saveProgress(1);
            VKUrlDto urlDto = VKUrlParser.parseUrl(url);
            wallPosts.put(urlDto.getElementId(),
                getWallService().getTopicCommentsWithLikesSync(userTask.copyNoCreate(),
                    urlDto.getOwnerId(), urlDto.getElementId()));
        }
        ArrayUtils utils = new ArrayUtils();
        Map<Integer, Integer> groupActivity = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> e : wallPosts.entrySet()) {
            userTask = userTask.saveProgress(1);
            groupActivity = utils.intersectWithCount(groupActivity, e.getValue());
        }
        groupActivity = ArrayUtils.sortByValue(groupActivity, minCount);
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(groupActivity.keySet(), "\n")));
        return groupActivity;
    }

    @Override
    @Async
    public Future<Map<Integer, Map<Integer, Integer>>> getActiveAuditory(UserTask settings, ActiveAuditoryDTO activeAuditoryDTO) {
        return new AsyncResult<>(getActiveAuditorySync(settings, activeAuditoryDTO));
    }

    private int countSteps(ActiveAuditoryDTO activeAuditoryDTO, Map<Integer, List<WallPost>> wallPosts) {
        int count = 0;
        for (Map.Entry<Integer, List<WallPost>> e : wallPosts.entrySet()) {
            count += e.getValue().size();
        }
        int mult = 3;
        if (!activeAuditoryDTO.getCountComments()) {
            mult--;
        }
        if (!activeAuditoryDTO.getCountLikes()) {
            mult--;
        }
        if (!activeAuditoryDTO.getCountReposts()) {
            mult--;
        }
        if (mult==0) {
            mult = 1;
        }
        count*=mult;
        return count + wallPosts.entrySet().size();
    }

    public WallService getWallService() {
        return wallService;
    }

    public void setWallService(WallService wallService) {
        this.wallService = wallService;
    }
}
