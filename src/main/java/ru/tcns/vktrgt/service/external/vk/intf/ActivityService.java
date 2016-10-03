package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.User;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Created by TIMUR on 30.04.2016.
 */
public interface ActivityService extends VKService {
    public static String BEAN_NAME = "ActivityServiceImpl";
    public static String ACTIVE_TOPIC_AUDITORY = BEAN_NAME + "TopicAuditory";
    public static String ACTIVE_AUDITORY = BEAN_NAME + "Auditory";
    Future<Map<Integer, Map<Integer, Integer>>> getActiveAuditory(UserTask task, ActiveAuditoryDTO activeAuditoryDTO);
    Future<Map<Integer, Integer>> getActiveTopicAuditory(UserTask task, List<String> topicUrls, Integer minCount);

    Map<Integer, Map<Integer, Integer>> getActiveAuditorySync(UserTask task, ActiveAuditoryDTO activeAuditoryDTO);
    Map<Integer, Integer> getActiveTopicAuditorySync(UserTask task, List<String> topicUrls, Integer minCount);
}
