package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.anno.JsonEntity;
import ru.tcns.vktrgt.domain.external.vk.internal.Comment;

import java.util.List;

/**
 * Created by TIMUR on 30.04.2016.
 */
@JsonEntity
public class CommentsResponse {
    private  Integer count;
    private List<Comment> items;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Comment> getItems() {
        return items;
    }

    public void setItems(List<Comment> items) {
        this.items = items;
    }
}
