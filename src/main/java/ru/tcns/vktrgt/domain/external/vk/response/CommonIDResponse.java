package ru.tcns.vktrgt.domain.external.vk.response;

import java.util.ArrayList;

/**
 * Created by TIMUR on 22.04.2016.
 */
public class CommonIDResponse {
    private Integer count;
    private ArrayList<Integer> ids;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<Integer> getIds() {
        return ids;
    }

    public void setIds(ArrayList<Integer> ids) {
        this.ids = ids;
    }
}
