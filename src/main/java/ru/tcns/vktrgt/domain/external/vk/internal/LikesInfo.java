package ru.tcns.vktrgt.domain.external.vk.internal;

import ru.tcns.vktrgt.anno.JsonEntity;

/**
 * Created by TIMUR on 29.04.2016.
 */
@JsonEntity
public class LikesInfo {
    private Integer count;
    private Integer userLikes;
    private Integer canLike;
    private Integer canPublish;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getUserLikes() {
        return userLikes;
    }

    public void setUserLikes(Integer userLikes) {
        this.userLikes = userLikes;
    }

    public Integer getCanLike() {
        return canLike;
    }

    public void setCanLike(Integer canLike) {
        this.canLike = canLike;
    }

    public Integer getCanPublish() {
        return canPublish;
    }

    public void setCanPublish(Integer canPublish) {
        this.canPublish = canPublish;
    }
}
