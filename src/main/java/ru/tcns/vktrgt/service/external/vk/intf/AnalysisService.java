package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.User;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by TIMUR on 01.05.2016.
 */
public interface AnalysisService {
    Future<AnalyseDTO> analyseUsers(UserTaskSettings settings, List<String> users, AnalyseDTO analyseDTO);
    Future<List<User>> filterUsers(UserTaskSettings settings, List<String> users, AnalyseDTO analyseDTO);
}
