package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.User;

import java.util.List;

/**
 * Created by TIMUR on 01.05.2016.
 */
public interface AnalysisService {
    AnalyseDTO analyseUsers(List<User> users, AnalyseDTO analyseDTO);
}
