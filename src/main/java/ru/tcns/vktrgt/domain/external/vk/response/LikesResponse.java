package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.anno.JsonEntity;

import java.util.List;

/**
 * Created by TIMUR on 30.04.2016.
 */
@JsonEntity
public class LikesResponse {
    private Integer count;
    private List<Integer> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }
}
