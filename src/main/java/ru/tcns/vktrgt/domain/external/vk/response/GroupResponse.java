package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.domain.external.vk.internal.Group;

import java.util.List;

/**
 * Created by timur on 3/28/16.
 */
public class GroupResponse {
    private int count;
    private List<Group> groups;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }
}
