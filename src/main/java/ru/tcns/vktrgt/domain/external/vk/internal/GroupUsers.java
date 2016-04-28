package ru.tcns.vktrgt.domain.external.vk.internal;

import ru.tcns.vktrgt.domain.external.vk.response.GroupUserResponse;

import java.util.ArrayList;

/**
 * Created by timur on 3/26/16.
 */
public class GroupUsers {
    private String groupId;
    private ArrayList<Integer> users;

    public GroupUsers(int count, String groupId) {
        users = new ArrayList<>(count);
        this.groupId = groupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public ArrayList<Integer> getUsers() {
        return users;
    }
    public void append(GroupUserResponse response) {
        if (response.getUserIds()!=null) {
            users.addAll(response.getUserIds());
        }

    }
}
