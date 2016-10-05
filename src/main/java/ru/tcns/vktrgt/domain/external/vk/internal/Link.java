package ru.tcns.vktrgt.domain.external.vk.internal;

import ru.tcns.vktrgt.anno.JsonEntity;

/**
 * Created by TIMUR on 06.10.2016.
 */
@JsonEntity
public class Link {
    private Integer id;
    private String url;
    private String name;
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

