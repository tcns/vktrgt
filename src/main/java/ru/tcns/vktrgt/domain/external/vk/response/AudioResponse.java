package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.domain.external.vk.internal.Audio;

import java.util.List;

/**
 * Created by TIMUR on 17.07.2016.
 */
public class AudioResponse {
    private Integer count;
    private List<Audio> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Audio> getItems() {
        return items;
    }

    public void setItems(List<Audio> items) {
        this.items = items;
    }
}
