package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.commons.lang.math.IntRange;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.Sex;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDefault;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.repository.UserTaskRepository;
import ru.tcns.vktrgt.service.export.impl.ExportService;
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;
import ru.tcns.vktrgt.service.external.vk.intf.VKUserService;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ExecutionException;
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
    @Inject
    private VKUserService vkUserService;
    @Inject
    private ExportService exportService;

    @Async
    @Override
    public Future<List<User>> filterUsers(UserTaskSettings settings, List<String> userIds, AnalyseDTO analyseDTO) {
        UserTask userTask = UserTask.create(FILTER_USERS, settings, repository);
        userTask = userTask.saveInitial(1);
        List<User> users = new ArrayList<>();
        try {
            users = getUsers(settings, userIds).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
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
        userTask.saveFinal(exportService.getStreamFromObject(users));
        return new AsyncResult<>(users);
    }

    private Future<List<User>> getUsers(UserTaskSettings settings, List<String> userIds) {
        return vkUserService.getUserInfo(new UserTaskSettings(settings, false), userIds);
    }


    @Override
    @Async
    public Future<AnalyseDTO> analyseUsers(UserTaskSettings settings, List<String> userIds, AnalyseDTO analyseDTO) {
        UserTask userTask = UserTask.create(ANALYSE_USERS, settings, repository);
        userTask = userTask.saveInitial(1);
        List<User> users = new ArrayList<>();
        try {
            users = getUsers(settings, userIds).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        collectFields(analyseDTO, users);
        if (analyseDTO.getAge() != null) {

            userTask = userTask.updateStatusMessage("Анализ по возрасту");
            for (IntRange range : analyseDTO.getAge().keySet()) {
                List<User> uList = new ArrayList<>();
                for (User user : users) {
                    if (user.getBdate() != null &&
                        !user.getBdate().isEmpty() &&
                        range.containsInteger(DateUtils.getAge(user.getBdate(), VKDicts.BDAY_FORMATS, 0)) ||
                        ((user.getBdate()==null || user.getBdate().isEmpty()) && range.equals(VKDefault.DEFAULT_RANGE))) {
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
                        user.getCity().getId().equals(city) ||
                        user.getCity()==null && city==VKDefault.CITY) {
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
                        user.getCountry().getId().equals(country) ||
                        user.getCountry() == null && country == VKDefault.COUNTRY) {
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
        userTask.saveFinal(exportService.getStreamFromObject(analyseDTO));
        return new AsyncResult<>(analyseDTO);
    }

    private void collectFields(AnalyseDTO analyseDTO, List<User> users) {
        if (analyseDTO.getCities() == null) {
            analyseDTO.setCities(new LinkedHashMap<>());
        }
        if (analyseDTO.getCountries() == null) {
            analyseDTO.setCountries(new LinkedHashMap<>());
        }
        if (analyseDTO.getSex() == null) {
            analyseDTO.setSex(new LinkedHashMap<>());
        }
        analyseDTO.getCities().put(VKDefault.CITY, new ArrayList<>());
        analyseDTO.getCountries().put(VKDefault.COUNTRY, new ArrayList<>());
        analyseDTO.getAge().put(VKDefault.DEFAULT_RANGE, new ArrayList<>());
        for (User user : users) {

            if (user.getCity() != null) {
                analyseDTO.getCities().put(user.getCity().getId(), new ArrayList<>());
            }
            if (user.getCountry() != null) {
                analyseDTO.getCountries().put(user.getCountry().getId(), new ArrayList<>());
            }

        }
        for (Sex sex : Sex.values()) {
            analyseDTO.getSex().put(sex.getIntValue(), new ArrayList<>());
        }
    }
}
