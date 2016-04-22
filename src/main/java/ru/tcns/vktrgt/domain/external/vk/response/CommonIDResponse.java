package ru.tcns.vktrgt.domain.external.vk.response;

import java.util.ArrayList;

/**
 * Created by TIMUR on 22.04.2016.
 */
public class CommonIDResponse {
    private Integer count;
    private ArrayList<Long> ids;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<Long> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Long> ids) {
        this.ids = ids;
    }
}
