package ru.tcns.vktrgt.domain.external.vk.internal;

import ru.tcns.vktrgt.anno.JsonEntity;

/**
 * Created by Тимур on 09.04.2016.
 */
@JsonEntity
public class Market {
    private Integer enabled;
    private String priceMin;
    private String priceMax;
    private Integer mainAlbumId;
    private Integer contactId;
    private Integer currencyId;
    private String currencyName;

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getPriceMin() {
        return priceMin;
    }

    public void setPriceMin(String priceMin) {
        this.priceMin = priceMin;
    }

    public String getPriceMax() {
        return priceMax;
    }

    public void setPriceMax(String priceMax) {
        this.priceMax = priceMax;
    }

    public Integer getMainAlbumId() {
        return mainAlbumId;
    }

    public void setMainAlbumId(Integer mainAlbumId) {
        this.mainAlbumId = mainAlbumId;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }
}
