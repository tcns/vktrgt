package ru.tcns.vktrgt.domain.external.vk.response;


import ru.tcns.vktrgt.domain.external.vk.internal.User;

import java.util.ArrayList;

/**
 * Created by TIMUR on 22.04.2016.
 */
public class FriendsResponse {
    private Integer count;
    private ArrayList<User> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<User> getItems() {
        return items;
    }

    public void setItems(ArrayList<User> items) {
        this.items = items;
    }
}
