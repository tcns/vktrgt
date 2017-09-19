package ru.tcns.vktrgt.service.external.vk.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.math.IntRange;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.domain.OperationTypes;
import ru.tcns.vktrgt.domain.UserTask;
import ru.tcns.vktrgt.domain.UserTaskSettings;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.Sex;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDefault;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.util.ArrayUtils;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.domain.util.parser.JsonParser;
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


    @Inject
    private VKUserService vkUserService;
    @Inject
    private ExportService exportService;

    @Async
    @Override
    public Future<List<User>> filterUsers(UserTask userTask, List<String> userIds, AnalyseDTO analyseDTO) {
        return new AsyncResult<>(filterUsersSync(userTask, userIds, analyseDTO));
    }

    @Override
    public AnalyseDTO analyseUsersSync(UserTask userTask, List<String> userIds, AnalyseDTO analyseDTO) {
        userTask = userTask.startWork();
        userTask = userTask.saveInitial(4);
        List<User> users = getUsers(userTask.copyNoCreate(), userIds);
        collectFields(analyseDTO, users);
        if (analyseDTO.getAge() != null) {
            userTask = userTask.saveProgress(1);
            userTask = userTask.updateStatusMessage("Анализ по возрасту");
            for (IntRange range : analyseDTO.getAge().keySet()) {
                List<User> uList = new ArrayList<>();
                for (User user : users) {
                    if (user.getBdate() != null &&
                        !user.getBdate().isEmpty() &&
                        range.containsInteger(DateUtils.getAge(user.getBdate(), VKDicts.BDAY_FORMATS, 0)) ||
                        ((user.getBdate() == null || user.getBdate().isEmpty()) && range.equals(VKDefault.DEFAULT_RANGE))) {
                        uList.add(user);
                    }
                }
                analyseDTO.getAge().put(range, uList);
            }
        }
        if (analyseDTO.getCities() != null) {
            userTask = userTask.saveProgress(1);
            userTask = userTask.updateStatusMessage("Анализ по городам");
            for (Integer city : analyseDTO.getCities().keySet()) {
                List<User> uList = new ArrayList<>();
                for (User user : users) {
                    if (user.getCity() != null &&
                        user.getCity().getId().equals(city) ||
                        user.getCity() == null && city == VKDefault.CITY) {
                        uList.add(user);
                    }
                }
                analyseDTO.getCities().put(city, uList);
            }
        }
        if (analyseDTO.getCountries() != null) {
            userTask = userTask.saveProgress(1);
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
            userTask = userTask.saveProgress(1);
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
        userTask = userTask.updateStatusMessage("Обработка результатов");
        userTask.saveFinal(exportService.getStreamFromObject(getFormattedAnalyseReport(analyseDTO)));
        return analyseDTO;
    }

    @Override
    public List<User> filterUsersSync(UserTask userTask, List<String> userIds, AnalyseDTO analyseDTO) {
        userTask = userTask.startWork();
        userTask = userTask.saveInitial(1);
        List<User> users = getUsers(userTask.copyNoCreate(), userIds);
        if (analyseDTO.getAge() != null && analyseDTO.getAge().keySet().size() > 0) {
            userTask = userTask.updateStatusMessage("Фильтрация по возрасту");
            for (IntRange range : analyseDTO.getAge().keySet()) {
                users = users.parallelStream().filter(a ->
                    a.getBdate() != null &&
                        !a.getBdate().isEmpty() &&
                        range.containsInteger(DateUtils.getAge(a.getBdate(), VKDicts.BDAY_FORMATS, 0))).collect(Collectors.toList());
            }
        }
        if (analyseDTO.getCities() != null && analyseDTO.getCities().keySet().size() > 0) {
            userTask = userTask.updateStatusMessage("Фильтрация по городу");
            users = users.parallelStream().filter(a ->
                a.getCity() != null &&
                    analyseDTO.getCities().keySet().contains(a.getCity().getId())).collect(Collectors.toList());
        }
        if (analyseDTO.getCountries() != null && analyseDTO.getCountries().keySet().size() > 0) {

            userTask = userTask.updateStatusMessage("Фильтрация по стране");
            users = users.parallelStream().filter(user ->
                user.getCountry() != null &&
                    analyseDTO.getCountries().keySet().contains(user.getCountry().getId())).collect(Collectors.toList());
        }
        if (analyseDTO.getSex() != null && analyseDTO.getSex().keySet().size() > 0) {
            userTask = userTask.updateStatusMessage("Фильтрация по полу");
            users = users.parallelStream().filter(user ->
                user.getSex() != null &&
                    analyseDTO.getSex().keySet().contains(user.getSex())).collect(Collectors.toList());
        }
        if (analyseDTO.getNeedInstagram()) {
            userTask = userTask.updateStatusMessage("Фильтрация по инстаграму");
            users = users.parallelStream().filter(user ->
                StringUtils.isNotEmpty(user.getInstagram())).collect(Collectors.toList());
        }
        if (analyseDTO.getNeedTwitter()) {
            userTask = userTask.updateStatusMessage("Фильтрация по твиттеру");
            users = users.parallelStream().filter(user ->
                StringUtils.isNotEmpty(user.getTwitter())).collect(Collectors.toList());
        }
        if (analyseDTO.getHasSibling()) {
            userTask = userTask.updateStatusMessage("Фильтрация по второй половине");
            users = users.parallelStream().filter(user -> user.getRelationPartner() != null).collect(Collectors.toList());
        }
        if (analyseDTO.getRelationships() != null && analyseDTO.getRelationships().keySet().size() > 0) {
            userTask = userTask.updateStatusMessage("Фильтрация типу второй половины");
            users = users.parallelStream().filter(user ->
                                                      user.getRelation() != null &&
                                                          analyseDTO.getRelationships().keySet().contains(user.getRelation())).collect(Collectors.toList());
        }

        List<Integer> idsFinal = users.parallelStream().map(User::getId).collect(Collectors.toList());
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(idsFinal, "\n")));
        return users;
    }

    @Override
    public List<String> listOperationSync(UserTask userTask, List<String> list1, List<String> list2, int operationType) {
        userTask = userTask.startWork();
        userTask = userTask.saveInitial(1);
        List<String> result = new ArrayList<>();
        switch (operationType) {
            case OperationTypes.DIFFERENCE:
                result = new ArrayList<>(Sets.newLinkedHashSet(ArrayUtils.xor(list1, list2)));
                break;
            case OperationTypes.INTERSECT:
                list1.retainAll(list2);
                result = list1;
                break;
            case OperationTypes.MERGE:
                list1.addAll(list2);
                result = new ArrayList<>(Sets.newLinkedHashSet(list1));
                break;
            case OperationTypes.MINUS:
                list1.removeAll(list2);
                result = list1;
                break;
        }
        userTask.saveProgress(1);
        userTask.saveFinal(exportService.getStreamFromObject(StringUtils.join(result, "\n")));
        return result;
    }

    @Override
    @Async
    public Future<List<String>> listOperation(UserTask userTask, List<String> list1, List<String> list2, int operationType) {
        return new AsyncResult<>(listOperationSync(userTask, list1, list2, operationType));
    }

    private List<User> getUsers(UserTask userTask, List<String> userIds) {
        return vkUserService.getUserInfoSync(userTask, userIds);
    }


    @Override
    @Async
    public Future<AnalyseDTO> analyseUsers(UserTask userTask, List<String> userIds, AnalyseDTO analyseDTO) {
        return new AsyncResult<>(analyseUsersSync(userTask, userIds, analyseDTO));
    }

    private String getFormattedPercent(float max, float val) {
        return String.format("%.2f", ((val / max) * 100.0)) + " %";
    }

    private String getFormattedAnalyseReport(AnalyseDTO analyseDTO) {
        StringBuilder sb = new StringBuilder();
        sb.append("Распределение по возрастам\n");
        ArrayUtils utils = new ArrayUtils();
        int max = analyseDTO.getUsers().size();
        Map<IntRange, Integer> ages = utils.sortByValue(ArrayUtils.getCounts(analyseDTO.getAge()), 0);
        for (Map.Entry<IntRange, Integer> e : ages.entrySet()) {
            sb.append("\t\t").append(e.getKey().getMinimumInteger()).append("-")
                .append(e.getKey().getMaximumInteger()).append(" ").append(e.getValue())
                .append(": ")
                .append(getFormattedPercent(max, e.getValue())).append("\n");
        }
        sb.append("Распределение по городам\n");
        Map<Integer, Integer> cities = utils.sortByValue(ArrayUtils.getCounts(analyseDTO.getCities()), 0);
        for (Map.Entry<Integer, Integer> e : cities.entrySet()) {
            String cityName = "Не указано";
            if (e.getKey() != VKDefault.CITY) {
                List<User> users = analyseDTO.getCities().get(e.getKey());
                if (users != null && !users.isEmpty()) {
                    cityName = analyseDTO.getCities().get(e.getKey()).get(0).getCity().getTitle();
                }
            }

            sb.append("\t\t").append(cityName).append(" ").append(e.getValue())
                .append(": ")
                .append(getFormattedPercent(max, e.getValue())).append("\n");
        }
        sb.append("Распределение по странам\n");
        Map<Integer, Integer> countries = utils.sortByValue(ArrayUtils.getCounts(analyseDTO.getCountries()), 0);
        for (Map.Entry<Integer, Integer> e : countries.entrySet()) {
            String countryName = "Не указано";
            if (e.getKey() != VKDefault.COUNTRY) {
                List<User> users = analyseDTO.getCountries().get(e.getKey());
                if (users != null && !users.isEmpty()) {
                    countryName = users.get(0).getCountry().getTitle();
                }
            }
            sb.append("\t\t").append(countryName).append(" ").append(e.getValue())
                .append(": ")
                .append(getFormattedPercent(max, e.getValue())).append("\n");
        }
        sb.append("Распределение по полу\n");
        Map<Integer, Integer> sex = utils.sortByValue(ArrayUtils.getCounts(analyseDTO.getSex()), 0);
        for (Map.Entry<Integer, Integer> e : sex.entrySet()) {
            String str = Sex.N.name();
            for (Sex s : Sex.values()) {
                if (s.getIntValue() == e.getKey()) {
                    str = s.toString();
                }
            }
            sb.append("\t\t").append(str).append(" ").append(e.getValue())
                .append(": ")
                .append(getFormattedPercent(max, e.getValue())).append("\n");
        }
        return sb.toString();

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
