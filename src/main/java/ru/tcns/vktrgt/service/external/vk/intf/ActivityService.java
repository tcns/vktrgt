package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.User;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.ActiveAuditoryDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 30.04.2016.
 */
public interface ActivityService {
    Map<Integer, Map<Integer, Integer>> getActiveAuditory(UserTaskSettings settings, ActiveAuditoryDTO activeAuditoryDTO);
    Map<Integer, Map<Integer, Integer>> getActiveTopicAuditory(UserTaskSettings settings, List<String> topicUrls, Integer minCount);
}
