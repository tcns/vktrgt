package ru.tcns.vktrgt.service.external.vk.impl;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.VKUrlDto;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.parser.VKUrlParser;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.external.vk.intf.ActivityService;
import ru.tcns.vktrgt.service.external.vk.intf.WallService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by TIMUR on 30.04.2016.
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    public static String BEAN_NAME = "ActivityServiceImpl";
    public static String ACTIVE_TOPIC_AUDITORY = BEAN_NAME + "TopicAuditory";
    public static String ACTIVE_AUDITORY = BEAN_NAME + "Auditory";

    @Inject
    private WallService wallService;
    @Inject
    private UserTaskRepository repository;

    @Override
    @Async
    public Future<Map<Integer, Map<Integer, Integer>>> getActiveTopicAuditory(UserTaskSettings settings, List<String> topicUrls, Integer minCount) {
        Map<Integer, Map<Integer, Integer>> activity = new HashMap<>();
        UserTask userTask = new UserTask(ACTIVE_TOPIC_AUDITORY, settings, repository);
        userTask = userTask.saveInitial(topicUrls.size() * 2);
        Map<Integer, List<Integer>> wallPosts = new HashMap<>();
        for (String url : topicUrls) {
            userTask = userTask.saveProgress(1);
            VKUrlDto urlDto = VKUrlParser.parseUrl(url);
            try {
                wallPosts.put(urlDto.getElementId(),
                    getWallService().getTopicCommentsWithLikes(
                        new UserTaskSettings(settings.getUser(), false, settings.getTaskDescription()),
                        urlDto.getOwnerId(), urlDto.getElementId()).get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        ArrayUtils utils = new ArrayUtils();
        for (Map.Entry<Integer, List<Integer>> e : wallPosts.entrySet()) {
            userTask = userTask.saveProgress(1);
            Map<Integer, Integer> groupActivity = new HashMap<>();
            groupActivity = utils.intersectWithCount(groupActivity, e.getValue());
            groupActivity = ArrayUtils.sortByValue(groupActivity, minCount);
            activity.put(e.getKey(), groupActivity);
        }
        userTask.saveFinal(activity);
        return new AsyncResult<>(activity);
    }

    @Override
    @Async
    public Future<Map<Integer, Map<Integer, Integer>>> getActiveAuditory(UserTaskSettings settings, ActiveAuditoryDTO activeAuditoryDTO) {
        Map<Integer, Map<Integer, Integer>> activity = new HashMap<>();
        UserTask userTask = new UserTask(ACTIVE_AUDITORY, settings, repository);
        Map<Integer, List<WallPost>> wallPosts = new HashMap<>();
        userTask = userTask.saveInitial(activeAuditoryDTO.getGroups().size());
        userTask = userTask.updateStatusMessage("Сбор постов со стены");
        for (String i : activeAuditoryDTO.getGroups()) {
            try {
                Integer val = Integer.valueOf(i);
                wallPosts.put(val, getWallService().getWallPosts(
                    new UserTaskSettings(settings.getUser(), false, settings.getTaskDescription()),
                    val, activeAuditoryDTO.getMaxDays()).get());
            } catch (NumberFormatException e){}
            catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        userTask = userTask.updateStatusMessage("Обработка постов");
        if (activeAuditoryDTO.getPostIds() != null &&
            !activeAuditoryDTO.getPostIds().isEmpty()) {
            List<WallPost> posts = new ArrayList<>();
            for (String id : activeAuditoryDTO.getPostIds()) {
                try {
                    Integer val = Integer.valueOf(id);
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
                    List<Integer> list = new ArrayList<>();
                    try {
                        list = getWallService().getComments(
                            new UserTaskSettings(settings.getUser(), false, settings.getTaskDescription()), e.getKey(), post.getId()).get();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }
                    groupActivity = utils.intersectWithCount(groupActivity, list);
                    userTask = userTask.saveProgress(1);
                }
                if (activeAuditoryDTO.getCountLikes()) {

                    userTask = userTask.updateStatusMessage("Сбор мне нравится");
                    List<Integer> list = new ArrayList<>();
                    try {
                        list = getWallService().getLikes(
                            new UserTaskSettings(settings.getUser(), false, settings.getTaskDescription()), e.getKey(), post.getId(), activeAuditoryDTO.getType()).get();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }
                    groupActivity = utils.intersectWithCount(groupActivity, list);
                    userTask = userTask.saveProgress(1);
                }
                if (activeAuditoryDTO.getCountReposts()) {
                    userTask = userTask.updateStatusMessage("Сбор репостов");

                    List<Integer> list = null;
                    try {
                        list = getWallService().getReposts(
                            new UserTaskSettings(settings.getUser(), false,
                                settings.getTaskDescription()), e.getKey(), post.getId()).get();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    } catch (ExecutionException e1) {
                        e1.printStackTrace();
                    }
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
        }
        userTask.saveFinal(activity);
        return new AsyncResult<>(activity) ;
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
