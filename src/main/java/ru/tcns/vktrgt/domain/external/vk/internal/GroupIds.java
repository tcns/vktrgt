package ru.tcns.vktrgt.domain.external.vk.internal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by TIMUR on 16.04.2016.
 */
@Document(collection = "vk_group_ids")
public class GroupIds implements Comparable<GroupIds> {
    public GroupIds(Integer id) {
        this.id = id;
    }
    @Id
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int compareTo(GroupIds o) {
        return this.getId().compareTo(o.getId());
    }
}
