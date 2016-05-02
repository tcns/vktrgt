package ru.tcns.vktrgt.service.external.vk.impl;

import org.apache.commons.lang.math.IntRange;
import ru.tcns.vktrgt.domain.external.vk.dict.AnalyseDTO;
import ru.tcns.vktrgt.domain.external.vk.dict.VKDicts;
import ru.tcns.vktrgt.domain.external.vk.internal.User;
import ru.tcns.vktrgt.domain.util.DateUtils;
import ru.tcns.vktrgt.service.external.vk.intf.AnalysisService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by TIMUR on 01.05.2016.
 */
public class AnalysisServiceImpl implements AnalysisService {
    @Override
    public AnalyseDTO analyseUsers(List<User> users, AnalyseDTO analyseDTO) {
        ExecutorService service = Executors.newFixedThreadPool(4);
        ArrayList<Future> tasks = new ArrayList<>();
        try {
            tasks.add(service.submit(() -> {
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
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tasks.add(service.submit(() -> {
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
            }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tasks.add(
                service.submit(() -> {
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
                }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            tasks.add(
                service.submit(() -> {
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
                }));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Future future: tasks) {
            try {
                future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        service.shutdown();
        return analyseDTO;
    }
}
