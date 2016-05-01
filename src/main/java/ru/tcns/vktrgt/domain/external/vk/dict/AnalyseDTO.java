package ru.tcns.vktrgt.domain.external.vk.dict;

import org.apache.commons.lang.math.IntRange;
import ru.tcns.vktrgt.domain.external.vk.internal.User;

import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 01.05.2016.
 */
public class AnalyseDTO {
    private Map<Integer, List<User>> sex;
    private Map<IntRange, List<User>> age;
    private Map<Integer, List<User>> cities;
    private Map<Integer, List<User>> countries;

    public Map<Integer, List<User>> getSex() {
        return sex;
    }

    public void setSex(Map<Integer, List<User>> sex) {
        this.sex = sex;
    }

    public Map<IntRange, List<User>> getAge() {
        return age;
    }

    public void setAge(Map<IntRange, List<User>> age) {
        this.age = age;
    }

    public Map<Integer, List<User>> getCities() {
        return cities;
    }

    public void setCities(Map<Integer, List<User>> cities) {
        this.cities = cities;
    }

    public Map<Integer, List<User>> getCountries() {
        return countries;
    }

    public void setCountries(Map<Integer, List<User>> countries) {
        this.countries = countries;
    }
}
