package ru.tcns.vktrgt.domain.external.vk.response;

import java.util.ArrayList;

/**
 * Created by TIMUR on 27.04.2016.
 */
public class SubscriptionsResponse {
    private String userId;
    private ArrayList<Integer> users;
    private ArrayList<Integer> groups;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Integer> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Integer> users) {
        this.users = users;
    }

    public ArrayList<Integer> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<Integer> groups) {
        this.groups = groups;
    }
}
