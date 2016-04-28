package ru.tcns.vktrgt.domain.external.vk.response;

import sun.reflect.generics.tree.Tree;

import java.util.ArrayList;

/**
 * Created by timur on 3/26/16.
 */
public class GroupUserResponse {
    private Long groupId;
    private Integer count;
    private ArrayList<Integer> userIds;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(ArrayList<Integer> userIds) {
        this.userIds = userIds;
    }

}
