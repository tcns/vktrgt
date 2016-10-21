package ru.tcns.vktrgt.service.external.vk.intf;

import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.internal.User;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by TIMUR on 01.05.2016.
 */
public interface AnalysisService extends VKService {
    String FILTER_USERS = "user-filter";
    String ANALYSE_USERS = "user-analyse";
    String LIST_OPERATION = "list-operation";
    Future<AnalyseDTO> analyseUsers(UserTask task, List<String> users, AnalyseDTO analyseDTO);
    Future<List<User>> filterUsers(UserTask task, List<String> users, AnalyseDTO analyseDTO);
    AnalyseDTO analyseUsersSync(UserTask task, List<String> users, AnalyseDTO analyseDTO);
    List<User> filterUsersSync(UserTask task, List<String> users, AnalyseDTO analyseDTO);
    List<String> listOperationSync(UserTask userTask, List<String> list1, List<String> list2, int operationType);
    Future<List<String>> listOperation(UserTask userTask, List<String> list1, List<String> list2, int operationType);
}
