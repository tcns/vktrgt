package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.commons.lang.math.IntRange;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by TIMUR on 01.05.2016.
 */
@Service
public class AnalysisServiceImpl implements AnalysisService {

    public final static String BEAN_NAME = "AnalysisServiceImpl";
    public final static String FILTER_USERS = BEAN_NAME + "filterUsers";
    public final static String ANALYSE_USERS = BEAN_NAME + "analyseUsers";

    @Inject
    private UserTaskRepository repository;

    @Async
    @Override
    public Future<List<User>> filterUsers(UserTaskSettings settings, List<User> users, AnalyseDTO analyseDTO) {
        UserTask userTask = new UserTask(FILTER_USERS, settings, repository);
        userTask = userTask.saveInitial(1);
        if (analyseDTO.getAge() != null) {
            userTask = userTask.updateStatusMessage("Фильтрация по возрасту");
            for (IntRange range : analyseDTO.getAge().keySet()) {
                users = users.parallelStream().filter(a ->
                    a.getBdate() != null &&
                        !a.getBdate().isEmpty() &&
                        range.containsInteger(DateUtils.getAge(a.getBdate(), VKDicts.BDAY_FORMATS, 0))).collect(Collectors.toList());
            }
        }
        if (analyseDTO.getCities() != null) {

            userTask = userTask.updateStatusMessage("Фильтрация по городу");
            for (Integer city : analyseDTO.getCities().keySet()) {
                users = users.parallelStream().filter(a ->
                    a.getCity() != null &&
                        a.getCity().getId().equals(city)).collect(Collectors.toList());
            }
        }
        if (analyseDTO.getCountries() != null) {

            userTask = userTask.updateStatusMessage("Фильтрация по стране");
            for (Integer country : analyseDTO.getCountries().keySet()) {
                users = users.parallelStream().filter(user ->
                    user.getCountry() != null &&
                        user.getCountry().getId().equals(country)).collect(Collectors.toList());
            }
        }
        if (analyseDTO.getSex() != null) {
            userTask = userTask.updateStatusMessage("Фильтрация по полу");
            for (Integer sex : analyseDTO.getSex().keySet()) {
                users = users.parallelStream().filter(user ->
                    user.getSex() != null &&
                        user.getSex().equals(sex)).collect(Collectors.toList());
            }
        }
        userTask.saveFinal(users);
        return new AsyncResult<>(users);
    }

    @Override
    @Async
    public Future<AnalyseDTO> analyseUsers(UserTaskSettings settings, List<User> users, AnalyseDTO analyseDTO) {
        UserTask userTask = new UserTask(ANALYSE_USERS, settings, repository);
        userTask = userTask.saveInitial(1);
        if (analyseDTO.getAge() != null) {

            userTask = userTask.updateStatusMessage("Анализ по возрасту");
            for (IntRange range : analyseDTO.getAge().keySet()) {
                List<User> uList = new ArrayList<>();
                for (User user : users) {
                    if (user.getBdate() != null &&
                        !user.getBdate().isEmpty() &&
                        range.containsInteger(DateUtils.getAge(user.getBdate(), VKDicts.BDAY_FORMATS, 0))) {
                        uList.add(user);
                    }
                }
                analyseDTO.getAge().put(range, uList);
            }
        }
        if (analyseDTO.getCities() != null) {

            userTask = userTask.updateStatusMessage("Анализ по городам");
            for (Integer city : analyseDTO.getCities().keySet()) {
                List<User> uList = new ArrayList<>();
                for (User user : users) {
                    if (user.getCity() != null &&
                        user.getCity().getId().equals(city)) {
                        uList.add(user);
                    }
                }
                analyseDTO.getCities().put(city, uList);
            }
        }
        if (analyseDTO.getCountries() != null) {

            userTask = userTask.updateStatusMessage("Анализ по странам");
            for (Integer country : analyseDTO.getCountries().keySet()) {
                List<User> uList = new ArrayList<>();
                for (User user : users) {
                    if (user.getCountry() != null &&
                        user.getCountry().getId().equals(country)) {
                        uList.add(user);
                    }
                }
                analyseDTO.getCountries().put(country, uList);
            }
        }
        if (analyseDTO.getSex() != null) {

            userTask = userTask.updateStatusMessage("Анализ по полу");
            for (Integer sex : analyseDTO.getSex().keySet()) {
                List<User> uList = new ArrayList<>();
                for (User user : users) {
                    if (user.getSex() != null &&
                        user.getSex().equals(sex)) {
                        uList.add(user);
                    }
                }
                analyseDTO.getSex().put(sex, uList);
            }
        }
        userTask.saveFinal(users);
        return new AsyncResult<>(analyseDTO);
    }
}
