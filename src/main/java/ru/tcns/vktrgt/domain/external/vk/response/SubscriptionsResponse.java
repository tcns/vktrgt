package ru.tcns.vktrgt.domain.external.vk.response;

import java.util.ArrayList;

/**
 * Created by TIMUR on 27.04.2016.
 */
public class SubscriptionsResponse {
    private String userId;
    private ArrayList<Long> users;
    private ArrayList<Long> groups;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Long> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Long> users) {
        this.users = users;
    }

    public ArrayList<Long> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Long> groups) {
        this.groups = groups;
    }
}
