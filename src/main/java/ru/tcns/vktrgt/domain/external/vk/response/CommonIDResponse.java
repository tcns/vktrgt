package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.anno.JsonEntity;

import java.util.ArrayList;

/**
 * Created by TIMUR on 22.04.2016.
 */
@JsonEntity
public class CommonIDResponse {
    private Integer count;
    private ArrayList<Integer> items;

    public Integer getCount() {
        if (count == null) {
            return 0;
        }
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<Integer> getItems() {
        return items;
    }

    public void setItems(ArrayList<Integer> items) {
        this.items = items;
    }
}
