package ru.tcns.vktrgt.domain.external.vk.dict;

import org.apache.commons.lang.math.IntRange;
import ru.tcns.vktrgt.domain.external.vk.internal.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by TIMUR on 01.05.2016.
 */
public class AnalyseDTO implements Serializable {
    private Map<Integer, List<User>> sex = new LinkedHashMap<>();
    private Map<IntRange, List<User>> age = new LinkedHashMap<>();
    private Map<Integer, List<User>> cities = new LinkedHashMap<>();
    private Map<Integer, List<User>> countries = new LinkedHashMap<>();
    private List<String> users = new ArrayList<>();
    private Boolean needTwitter = false;
    private Boolean needInstagram = false;
    private Boolean hasSibling = false;
    private Map<Integer, List<User>> relationships = new LinkedHashMap<>();
    private String taskInfo = "";

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

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(String taskInfo) {
        this.taskInfo = taskInfo;
    }

    public Boolean getNeedTwitter() {
        return needTwitter;
    }

    public void setNeedTwitter(Boolean needTwitter) {
        this.needTwitter = needTwitter;
    }

    public Boolean getNeedInstagram() {
        return needInstagram;
    }

    public void setNeedInstagram(Boolean needInstagram) {
        this.needInstagram = needInstagram;
    }

    public Boolean getHasSibling() {
        return hasSibling;
    }

    public void setHasSibling(Boolean hasSibling) {
        this.hasSibling = hasSibling;
    }

    public Map<Integer, List<User>> getRelationships() {
        return relationships;
    }

    public void setRelationships(Map<Integer, List<User>> relationships) {
        this.relationships = relationships;
    }
}
