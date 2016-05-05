package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.anno.JsonEntity;
import ru.tcns.vktrgt.domain.external.vk.internal.Group;

import java.util.List;

/**
 * Created by timur on 3/28/16.
 */
@JsonEntity
public class GroupResponse {
    private int count;
    private List<Group> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Group> getItems() {
        return items;
    }

    public void setItems(List<Group> items) {
        this.items = items;
    }
}
