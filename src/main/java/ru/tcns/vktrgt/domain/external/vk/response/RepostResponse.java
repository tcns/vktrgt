package ru.tcns.vktrgt.domain.external.vk.response;

import ru.tcns.vktrgt.anno.JsonEntity;
import ru.tcns.vktrgt.domain.external.vk.internal.WallPost;

import java.util.List;

/**
 * Created by TIMUR on 30.04.2016.
 */
@JsonEntity
public class RepostResponse {
    //TODO: extend model
    private List<WallPost> items;

    public List<WallPost> getItems() {
        return items;
    }

    public void setItems(List<WallPost> items) {
        this.items = items;
    }
}
