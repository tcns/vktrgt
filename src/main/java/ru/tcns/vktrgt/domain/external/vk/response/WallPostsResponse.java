package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.anno.JsonEntity;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;

import java.util.List;

/**
 * Created by TIMUR on 29.04.2016.
 */
@JsonEntity
public class WallPostsResponse {
    private Integer count;
    private List<WallPost> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<WallPost> getItems() {
        return items;
    }

    public void setItems(List<WallPost> items) {
        this.items = items;
    }
}
