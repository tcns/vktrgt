package ru.tcns.vktrgt.domain.external.vk.dict;

/**
 * Created by TIMUR on 30.04.2016.
 */
public class VKUrlDto {
    private String type;
    private Integer ownerId;
    private Integer elementId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = Integer.valueOf(ownerId);
    }

    public Integer getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = Integer.valueOf(elementId);
    }
}
