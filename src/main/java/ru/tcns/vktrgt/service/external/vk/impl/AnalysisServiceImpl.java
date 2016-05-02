package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.commons.lang.math.IntRange;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by TIMUR on 01.05.2016.
 */
public class AnalysisServiceImpl implements AnalysisService {
    @Override
    public List<User> filterUsers(List<User> users, AnalyseDTO analyseDTO) {
        if (analyseDTO.getAge() != null) {
            for (IntRange range : analyseDTO.getAge().keySet()) {
                users = users.parallelStream().filter(a ->
                    a.getBdate() != null &&
                        !a.getBdate().isEmpty() &&
                        range.containsInteger(DateUtils.getAge(a.getBdate(), VKDicts.BDAY_FORMATS, 0))).collect(Collectors.toList());
            }
        }
        if (analyseDTO.getCities() != null) {
            for (Integer city : analyseDTO.getCities().keySet()) {
                users = users.parallelStream().filter(a ->
                    a.getCity() != null &&
                        a.getCity().getId().equals(city)).collect(Collectors.toList());
            }
        }
        if (analyseDTO.getCountries() != null) {
            for (Integer country : analyseDTO.getCountries().keySet()) {
                users = users.parallelStream().filter(user ->
                    user.getCountry() != null &&
                        user.getCountry().getId().equals(country)).collect(Collectors.toList());
            }
        }
        if (analyseDTO.getSex() != null) {
            for (Integer sex : analyseDTO.getSex().keySet()) {
                users = users.parallelStream().filter(user ->
                    user.getSex() != null &&
                        user.getSex().equals(sex)).collect(Collectors.toList());
            }
        }
        return users;
    }

    @Override
    public AnalyseDTO analyseUsers(List<User> users, AnalyseDTO analyseDTO) {
        if (analyseDTO.getAge() != null) {
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
        return analyseDTO;
    }
}
